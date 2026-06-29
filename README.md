# ofd-kt-proto

[![Maven Central](https://img.shields.io/maven-central/v/io.github.texport/ofd-kt-proto.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.texport/ofd-kt-proto)
[![Version](https://img.shields.io/badge/version-2.0.4-blue.svg)](https://github.com/texport/ofd-kt-proto/releases)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![CI Build](https://img.shields.io/github/actions/workflow/status/texport/ofd-kt-proto/ci.yml?branch=main&label=CI%20Build)](https://github.com/texport/ofd-kt-proto/actions)

---

### [Documentation in English](#documentation-in-english) &middot; [Документация на русском языке](#документация-на-русском-языке)

---

> [!IMPORTANT]
> **Disclaimer:** This is an unofficial, community-maintained library. It is not officially endorsed by, affiliated with, or sponsored by JSC "KazakhTelecom" or the State Revenue Committee of the Republic of Kazakhstan. All product names, logos, and brands are property of their respective owners.
> 
> **Дисклеймер:** Данный проект является неофициальной библиотекой, поддерживаемой сообществом. Он не связан, не спонсируется и не утверждался АО «Казахтелеком» или Комитетом государственных доходов РК. Все названия продуктов, товарные знаки и логотипы принадлежат их законным владельцам.

---

## Documentation in English

A Kotlin Multiplatform (KMP) library containing code entities generated from KazakhTelecom OFD (State Revenue Committee of the Republic of Kazakhstan) `.proto` files. 

The current protocol version is **2.0.3** (module `proto-v203`). These proto files implement the data transmission protocol between Cash Registers (KKM) and the OFD.

### Key Features
- **Kotlin Multiplatform Support**: Compiles to standard JVM bytecode (compatible with Java 17+ and Android) and Apple/iOS Native targets.
- **Type-safe Kotlin Protobuf bindings**: Uses Square Wire Gradle plugin to generate KMP-compliant code without JVM/Java runtime dependencies.
- **Modular design**: Designed to support multiple protocol versions side-by-side in the same project without package collisions.

### Installation

#### 1. Via Maven Central (Recommended)
Add the dependency to your shared `commonMain` source set inside `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("io.github.texport:ofd-kt-proto:2.0.4")
            }
        }
    }
}
```

#### 2. Local Publish (Local Development)
To build and publish to your local maven repository for testing:
```bash
./gradlew publishToMavenLocal
```

---

## Документация на русском языке

Мультиплатформенная библиотека Kotlin Multiplatform (KMP). Содержит сущности, сгенерированные из `.proto` файлов ОФД Казахтелеком (Комитет государственных доходов РК). 

Текущая версия протокола — **2.0.3** (модуль `proto-v203`). Прото-файлы реализуют протокол передачи данных ККМ в ОФД.

### Ключевые преимущества
- **Поддержка Kotlin Multiplatform**: Компилируется под JVM (совместима с Java 17+ и Android) и Apple/iOS Native таргеты.
- **Типизированные KMP биндинги**: Использует плагин Square Wire для кодогенерации чистых Kotlin-классов без привязки к Java SDK.
- **Поддержка параллельных версий**: Несколько версий протокола могут одновременно использоваться в одном проекте без конфликтов пакетов.

### Подключение библиотеки

#### 1. Через Maven Central (Рекомендуемый способ)
Добавьте зависимость в ваш общий набор исходников `commonMain` в `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("io.github.texport:ofd-kt-proto:2.0.4")
            }
        }
    }
}
```

#### 2. Локальная сборка и публикация
Для сборки и публикации в локальный Maven-репозиторий выполните команду:
```bash
./gradlew publishToMavenLocal
```
