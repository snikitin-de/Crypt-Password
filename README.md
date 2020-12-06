# Crypt-password

Программа, которая случайным образом по заданным параметрам создает пароли для пользователей. Она позволяет генерировать сложные пароли длинной 25 символов, содержащих латинские буквы нижнего регистра, одну букву верхнего регистра и один специальный символ - `%`. Особенность программы заключается в том, что можно запомнить кодовое слово и пароль и генерировать пароли для разных приложений и сайтов, не запоминая для каждого сайта свой пароль.

![Скриншот основного окна программы](.github/readme-images/crypt-password-main.jpg)

# Установка

Приложение находится в разработке, для установки приложения необходимо склонировать репозиторий к себе и скомпилировать проект с помощью `gradle` в терминале или через интерфейс Android Studio.

## Gradle 

В терминале на Windows ввести:

`gradlew build`

В терминале на Mac и Linux:

`./gradlew build`

## Android Studio

`Build -> Build Bundle(s) / APK(s) -> Build APK(s)`
