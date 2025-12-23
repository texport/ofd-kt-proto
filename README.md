# ofd-kt-proto

Библиотека для использования в Java и Kotlin проектах. Содержит сущности,
сгенерированные из `.proto` файлов ОФД Казахтелеком. Текущая версия протокола
— 2.0.3. Прото‑файлы реализуют протокол передачи данных ККМ в ОФД, протокол
разрабатывает КГД Республики Казахстан.

## Что внутри
- `src/main/proto/` — исходные `.proto`.
- `build/generated/` — сгенерированный код (не коммитится).
- `build/libs/ofd-kt-proto-2.0.3.jar` — итоговый артефакт.

## Сборка библиотеки
```bash
./gradlew build
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
    implementation("org.epicsquad.kkm:ofd-kt-proto:2.0.3")
}
```

Gradle сам соберет зависимость из исходников локально.

## Прямое подключение JAR
```kotlin
dependencies {
    implementation(files("path/to/ofd-kt-proto-2.0.3.jar"))
}
```

