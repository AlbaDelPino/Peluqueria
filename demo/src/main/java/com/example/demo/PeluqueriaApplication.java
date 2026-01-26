package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EntityScan(basePackages = "com.example.demo.domain") // 👈 aquí
public class PeluqueriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeluqueriaApplication.class, args);
	}

}

/*

http://localhost:8082/api/auth/signup/admin

{

  "username": "admin1564",
  "nombre": "Administrador General",
  "email": "admin55654@empresa.com",
  "contrasenya": "admin1263",
  "especialidad": "Gfsdfsdf"
}
http://localhost:8082/api/auth/signin

{
  "username": "admin14",
  "contrasenya": "admin123"

}


token geaders

Authorization: Bearer <token>

delete
http://localhost:8082/api/admins/1
get

http://localhost:8082/api/admins

put
http://localhost:8082/api/admins/2
{
    "id": 2,
    "username": "admin2r",
    "nombre": "Super Admin",
    "email": "superadmin@empresa.com",
    "contrasenya": "super123",
    "role": "ROLE_ADMIN",
    "especialidad": "Infraestructura"
}


//citas disponibles

http://localhost:8082/citas/disponibilidad?fecha=2026-01-12&hora=08:00:00

//crear cita

http://localhost:8082/citas/reservar

{
    "fecha": "2025-12-29",
    "hora": "14:00:00",
    "horario": {
        "id": 6,


			"cliente": {
			"id": 20
			}
			}

 */
