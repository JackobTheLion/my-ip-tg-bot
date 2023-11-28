### :question: Что это?
Небольшое приложение-бот, которое позволяет узнать внешний ip адрес сервера, на котором установлено.

Может быть полезно на домашних серверах с динамическим ip.

### :star: Возможности приложения:
Для получения ip нужно отправить боту команду `/ip`

После развертывания также будет доступен эндпоинт http://SERVER_ADDRESS/ip:PORT, где SERVER_ADDRESS - локальный адрес сервера. \
Порт по умолчанию 8080.

Образ доступен на [docker.hub](https://hub.docker.com/repository/docker/jackobthelion/my-ip-tg-bot/general)

Для запуска необходимо:
1. Зарегистрировать бота (https://telegram.me/BotFather)
2. Узнать ваш с ботом чат id. Для этого:
    * написать своему боту
    * перейти в браузере по ссылке: https://api.telegram.org/bot(TOKEN)/getUpdates где вместо токен подставить (TOKEN) своего бота
    * в полученном ответе найти `"chat":{"id":123456789,"first_name":name,"username":"userName","type":"private"}`
    * поле id и есть чат id

3. Перед запуском указать необходимые переменные окружения:
~~~ 
BOT_NAME - имя бота
BOT_TOKEN - токен
BOT_ADMIN_CHAT - ID вашего чата с ботом 
~~~
___
### :gear: Технологический стек
* Java 11
* Spring Boot
* telegrambots
* Lombok
* Maven
* Docker
___
