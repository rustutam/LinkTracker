![Build](https://github.com/central-university-dev/backend-academy-2025-spring-template/actions/workflows/build.yaml/badge.svg)

# Link Tracker


Приложение для отслеживания обновлений контента по ссылкам.
При появлении новых событий отправляется уведомление в Telegram.

Проект написан на `Java 23` с использованием `Spring Boot 3`.

Проект состоит из 2-х приложений:
* Bot
* Scrapper

Для работы требуется БД `PostgreSQL`. Присутствует опциональная зависимость на `Kafka`.

Для запуска добавить в переменные окружения

для бота: TELEGRAM_TOKEN

для скраппера: GITHUB_TOKEN, SO_TOKEN_KEY, SO_ACCESS_TOKEN

Для дополнительной справки: [HELP.md](./HELP.md)
