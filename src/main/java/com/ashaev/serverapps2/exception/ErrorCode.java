package com.ashaev.serverapps2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //? Auth (1000+)
    USER_NOT_FOUND(1001, HttpStatus.NOT_FOUND, "Пользователь с ID %s не найден"),
    USER_ALREADY_EXISTS(1002, HttpStatus.CONFLICT, "Пользователь с логином %s уже существует"),
    INVALID_PASSWORD(1003, HttpStatus.UNAUTHORIZED, "Неверный пароль"),
    REFRESH_TOKEN_EXPIRED(1004, HttpStatus.UNAUTHORIZED, "Refresh токен истек или невалиден"),
    ACCOUNT_DISABLED(1005, HttpStatus.FORBIDDEN, "Учетная запись заблокирована"),

    //? Academic (2000+)
    TEACHER_NOT_FOUND(2000, HttpStatus.NOT_FOUND, "Преподаватель с ID %s не найден"),
    LESSON_NOT_FOUND(2001, HttpStatus.NOT_FOUND, "Занятие с ID %s не найдено"),
    GROUP_NOT_FOUND(2002, HttpStatus.NOT_FOUND, "Группа с ID %s не найдена"),
    SUBJECT_NOT_FOUND(2003, HttpStatus.NOT_FOUND, "Дисциплина с ID %s не найдена"),
    SCHEDULE_CONFLICT(2004, HttpStatus.CONFLICT, "Конфликт расписания: время %s уже занято"),
    GROUP_IS_FULL(2005, HttpStatus.BAD_REQUEST, "В группе достигнут лимит студентов"),
    STUDENT_NOT_FOUND(2006, HttpStatus.NOT_FOUND, "Студент с ID %s не найден"),

    //? Attendance (3000+)
    ATTENDANCE_NOT_FOUND(3001, HttpStatus.NOT_FOUND, "Запись о посещаемости не найдена"),
    ATTENDANCE_ALREADY_MARKED(3002, HttpStatus.CONFLICT, "Посещаемость на это занятие уже проставлена"),
    INVALID_ATTENDANCE_STATUS(3003, HttpStatus.BAD_REQUEST, "Некорректный статус посещаемости: %s"),

    //? Validation (4000+)
    INVALID_INPUT(4001, HttpStatus.BAD_REQUEST, "Некорректные входные данные: %s"),
    DATE_OUT_OF_RANGE(4002, HttpStatus.BAD_REQUEST, "Выбранная дата вне допустимого диапазона"),
    FILE_UPLOAD_ERROR(4003, HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при обработке файла"),

    //? Resources (5000+)
    FILE_NOT_FOUND(5001, HttpStatus.NOT_FOUND, "Файл с ID %s не найден"),
    FILE_TOO_LARGE(5002, HttpStatus.PAYLOAD_TOO_LARGE, "Файл слишком большой. Максимальный размер: %s"),
    UNSUPPORTED_FILE_TYPE(5003, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Формат файла %s не поддерживается"),

    //? Entity (6000+)
    ENTITY_IS_LOCKED(6001, HttpStatus.LOCKED, "Сущность %s заблокирована для редактирования"),
    CANNOT_DELETE_ACTIVE_ENTITY(6002, HttpStatus.CONFLICT, "Невозможно удалить активную сущность: %s"),
    INVALID_STATE_TRANSITION(6003, HttpStatus.CONFLICT, "Недопустимый переход состояния: из %s в %s"),

    //? Limits (7000+)
    RATE_LIMIT_EXCEEDED(7001, HttpStatus.TOO_MANY_REQUESTS, "Слишком много запросов. Попробуйте позже"),
    ACTION_NOT_ALLOWED_FOR_ROLE(7002, HttpStatus.FORBIDDEN, "Действие запрещено для роли: %s"),
    TEMPORARY_UNAVAILABLE(7003, HttpStatus.SERVICE_UNAVAILABLE, "Сервис временно недоступен"),

    //? (8000+)
    PRECONDITION_FAILED(8001, HttpStatus.PRECONDITION_FAILED, "Не выполнены условия для выполнения операции: %s"),
    DEPENDENCY_VIOLATION(8002, HttpStatus.CONFLICT, "Невозможно удалить, так как есть зависимые данные: %s"),
    MISSING_REQUIRED_FIELD(8003, HttpStatus.BAD_REQUEST, "Отсутствует обязательное поле: %s"),

    //? Global (9000+)
    ACCESS_DENIED(9001, HttpStatus.FORBIDDEN, "У вас нет прав для выполнения этой операции"),
    INTERNAL_SERVER_ERROR(9999, HttpStatus.INTERNAL_SERVER_ERROR, "Произошла непредвиденная ошибка сервера");

    private final int code;
    private final HttpStatus status;
    private final String message;

    public String getFormattedMessage(Object... args) {
        return String.format(this.message, args);
    }
}