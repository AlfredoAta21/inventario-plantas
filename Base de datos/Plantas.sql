USE Catalogo;

CREATE TABLE Usuario (
                         Nombre VARCHAR(20) PRIMARY KEY,
                         Contraseña VARCHAR(255) NOT NULL,
                         Imagen LONGBLOB,
                         esAdmin BOOLEAN DEFAULT FALSE
);

SELECT * FROM USUARIO;

INSERT INTO Usuario (Nombre, Contraseña, Imagen, esAdmin)
VALUES ('Miyu', '123', NULL, TRUE);

DROP PROCEDURE validacion_usuario;

/*Valida el usuario para el login*/
DELIMITER //
CREATE PROCEDURE validacion_usuario(Nombre_User varchar(20), Contraseña_user varchar(255))
BEGIN
SELECT *
FROM Usuario
WHERE Nombre = Nombre_User
  AND Contraseña = Contraseña_user;
END //
DELIMITER ;


DROP PROCEDURE obtener_admin;

/*Valida el usuario para el login*/
DELIMITER //
CREATE PROCEDURE obtener_admin(Nombre_User varchar(20), Contraseña_user varchar(255))
BEGIN
SELECT CASE WHEN esAdmin = 1 THEN 1 ELSE 0 END AS esAdmin
FROM Usuario
WHERE Nombre = Nombre_User
  AND Contraseña = Contraseña_user;
END //
DELIMITER ;


