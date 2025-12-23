# ofd-kt-proto

Библиотека для использования в Java и Kotlin проектах. Содержит сущности,
сгенерированные из `.proto` файлов ОФД Казахтелеком. Текущая версия протокола
— 2.0.3 (модуль `proto-v203`). Прото‑файлы реализуют протокол передачи данных ККМ в ОФД, протокол
разрабатывает КГД Республики Казахстан.
Для других ОФД предполагаются отдельные репозитории и артефакты, чтобы имена не пересекались.

## Что внутри
- `proto-v203/src/main/proto/` — исходные `.proto` версии 2.0.3.
- `proto-v203/build/generated/` — сгенерированный код (не коммитится).
- `proto-v203/build/libs/ofd-kt-proto-v203-2.0.3.jar` — итоговый артефакт.

## Сборка библиотеки
```bash
./gradlew :ofd-kt-proto-v203:build
```

## Подключение из центрального проекта через Git
Если центральный проект забирает репозиторий с GitHub и собирает его вместе
с основным кодом, удобно использовать Gradle Composite Build.

1) Добавьте репозиторий `ofd-kt-proto` рядом с центральным проектом.
2) В `settings.gradle.kts` центрального проекта:
```kotlin
includeBuild("../ofd-kt-proto")
```
3) В `build.gradle.kts` центрального проекта:
```kotlin
dependencies {
    implementation("kz.kazakhtelecom:ofd-kt-proto-v203:2.0.3")
}
```

Gradle сам соберет зависимость из исходников локально.

Если нужно использовать две версии одновременно в одном проекте:
1) Оставить одинаковый `artifactId` нельзя — Gradle выберет только одну версию.
2) Есть два корректных варианта:
   - разные `artifactId` (см. раздел «Добавление новой версии протокола»);
   - подключение двух JAR напрямую через `files(...)`.

Пример с разными `artifactId`:
```kotlin
dependencies {
    implementation("kz.kazakhtelecom:ofd-kt-proto-v203:2.0.3")
    implementation("kz.kazakhtelecom:ofd-kt-proto-v204:2.0.4")
}
```

Пример с двумя JAR:
```kotlin
dependencies {
    implementation(files("path/to/ofd-kt-proto-v203-2.0.3.jar"))
    implementation(files("path/to/ofd-kt-proto-v204-2.0.4.jar"))
}
```

## Прямое подключение JAR
```kotlin
dependencies {
    implementation(files("path/to/ofd-kt-proto-v203-2.0.3.jar"))
}
```

## Добавление новой версии протокола
1) Скопируйте модуль `proto-v203` в новый, например `proto-v204`.
2) В `proto-v204/build.gradle.kts` обновите `version`, например `2.0.4`.
3) В `settings.gradle.kts` добавьте `include("proto-v204")`.
4) В `settings.gradle.kts` задайте имя проекта (artifactId) для новой версии:
   - `project(":proto-v204").name = "ofd-kt-proto-v204"`
   Это позволит подключать обе версии одновременно по координатам.
5) В `.proto` файлах нового модуля обновите неймспейсы:
   - `option java_package = "kz.kazakhtelecom.proto.v204";`
   - `package kz.kazakhtelecom.proto.v204;`
   - для update‑server:
     - `option java_package = "kz.kazakhtelecom.fd_svc.proto.v204";`
     - `package kz.kazakhtelecom.fd_svc.proto.v204;`
6) Обновите этот README (путь, версия, команда сборки).
