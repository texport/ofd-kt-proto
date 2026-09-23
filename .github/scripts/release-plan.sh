#!/usr/bin/env bash
# Какие модули протокола выпускать и под какими номерами.
#
# Версия модуля — версия протокола (val protocol в его сборке) и номер
# сборки через дефис: 2.0.4-1, 2.0.4-2, … Номер — следующий после последней
# метки этого протокола; метка v2.0.3 без номера считается сборкой 0.
#
# EVENT=push         — поставлена метка REF_NAME руками: выпускается модуль
#                      её протокола под её номером.
# EVENT=workflow_run — зелёный CI на main для коммита SHA: выпускается каждый
#                      модуль, который ещё не выпускался или менялся с последней
#                      своей метки; AUTO_RELEASE=false выключает это.
#
# Пишет в GITHUB_OUTPUT modules — JSON-список {name, dir, version, tag}.
set -euo pipefail

note() {
    echo "::notice title=Выпуск::$1"
    echo "- $1" >> "${GITHUB_STEP_SUMMARY:-/dev/null}"
}

if [ "$EVENT" != push ] && [ "${AUTO_RELEASE:-true}" != true ]; then
    note "Автоматический выпуск выключен переменной репозитория AUTO_RELEASE"
    echo "modules=[]" >> "$GITHUB_OUTPUT"
    exit 0
fi

modules='[]'
add() {
    modules=$(jq -c --arg name "$1" --arg dir "$2" --arg version "$3" \
        '. + [{name: $name, dir: $dir, version: $version, tag: ("v" + $version)}]' <<< "$modules")
}

for build in */build.gradle.kts; do
    protocol=$(sed -n 's/^val protocol = "\(.*\)"$/\1/p' "$build")
    [ -n "$protocol" ] || continue
    dir=$(dirname "$build")
    name=$(sed -n "s/^project(\":$dir\").name = \"\(.*\)\"$/\1/p" settings.gradle.kts)
    esc=${protocol//./\\.}

    if [ "$EVENT" = push ]; then
        version=${REF_NAME#v}
        if printf '%s' "$version" | grep -Eq "^$esc-[0-9]+$"; then add "$name" "$dir" "$version"; fi
        continue
    fi

    # Последняя метка протокола: «номер имя», самый большой номер.
    last=$(git tag -l "v$protocol" "v$protocol-*" \
        | sed -nE "s/^(v$esc)$/0 \1/p; s/^(v$esc-([0-9]+))$/\2 \1/p" \
        | sort -n | tail -1)
    number=${last%% *}
    last_tag=${last#* }
    if [ -n "$last" ] && [ -z "$(git log -1 --format=%H "$last_tag..$SHA" -- "$dir")" ]; then
        note "$name: изменений после $last_tag нет, выпуск не нужен"
        continue
    fi
    version="$protocol-$(( ${number:-0} + 1 ))"
    note "$name: выпуск v$version"
    add "$name" "$dir" "$version"
done

if [ "$EVENT" = push ] && [ "$modules" = '[]' ]; then
    echo "::error::Метка $REF_NAME не совпала с протоколом ни одного модуля"
    exit 1
fi
echo "modules=$modules" >> "$GITHUB_OUTPUT"
