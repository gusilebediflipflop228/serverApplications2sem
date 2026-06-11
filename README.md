# [Система учета посещаемости занятий студентами]

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-brightgreen)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue)](https://www.docker.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)]()

[Краткое описание проекта: "Система управления посещаемостью для отслеживания студентов/сотрудников".]

## 🚀 Функционал
* **Авторизация:** JWT-аутентификация.
* **Управление данными:** [например, CRUD операции с уроками/пользователями].
* **Swagger:** Документация API доступна по адресу `/swagger-ui.html`.

## 🛠 Технологии
* Java 21
* Spring Boot 3.4.1
* PostgreSQL 15
* Docker & Docker Compose

## 📦 Как запустить проект

Для работы приложения вам потребуется установленный **Docker**.

### Шаг 1: Клонирование репозитория
```bash
git clone [ссылка_на_ваш_репозиторий]
cd [имя_папки_проекта]
```
### Шаг 2: Запуск через Docker
```
docker compose up --build
```
### Шаг 3: Проверка
После запуска приложение будет доступно по адресу:
http://localhost:8080/swagger-ui.html
