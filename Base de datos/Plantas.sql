USE Catalogo;

CREATE TABLE Usuario (
    Nombre VARCHAR(20) PRIMARY KEY,
    Contraseña VARCHAR(255) NOT NULL,
    Imagen LONGBLOB,
    Adminis BOOLEAN DEFAULT FALSE
);

SELECT * FROM USUARIO;

INSERT INTO Usuario (Nombre, Contraseña, Imagen, Adminis)
VALUES ('Miyu', '123', NULL, TRUE);

 DROP PROCEDURE validacion_usuario;

/*Valida el usuario para el login*/
DELIMITER //
CREATE PROCEDURE validacion_usuario(Nombre_User varchar(20), Contraseña_user varchar(255), Adminis_user BOOLEAN)
	BEGIN
		SELECT * 
        FROM Usuario 
        WHERE Nombre = Nombre_User
        AND Contraseña = Contraseña_user
        AND Adminis = Adminis_user;
	END //
DELIMITER ;
