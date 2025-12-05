# TP2 Authentification avec JWT

## Description
Ce projet Spring Boot (Java 21) met en place une authentification stateless via JSON Web Token (JWT) en utilisant Spring Security. Les utilisateurs sont définis en mémoire (`user/password`, `admin/admin123`). Un login sur `/api/auth/login` retourne un JWT, utilisé ensuite en header `Authorization: Bearer ...` pour accéder aux endpoints protégés, par exemple `/api/hello`.

## Dépendances principales
- `spring-boot-starter-web` pour l’API REST.
- `spring-boot-starter-security` pour la sécurité et les filtres.
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson` pour la génération et la validation des JWT.

## Configuration
Paramètres attendus dans `application.properties` (ou équivalent) :
```
app.jwt.secret=<votre_cle_secrete_au_moins_256_bits>
app.jwt.expiration-ms=3600000
```

## Endpoints
- `POST /api/auth/login` : accepte un JSON `{ "username": "...", "password": "..." }`, authentifie et renvoie `{ "token": "<jwt>" }`.
- `GET /api/hello` : protégé ; nécessite le header `Authorization: Bearer <jwt>`.

## Filtres et sécurité
- `JwtAuthFilter` : extrait le JWT du header `Authorization`, valide le token via `JwtService` et peuple le `SecurityContext`.
- `SecurityConfig` : autorise `/api/auth/**`, protège le reste, enregistre le filtre JWT avant `UsernamePasswordAuthenticationFilter`.

## Tests réalisés (captures dans `screens/`)
- `img1.png` : requête POST `/api/auth/login` avec corps JSON (`user/password`).
- `img2.png` : réponse 200 avec le token JWT généré.
- `img3.png` : requête GET `/api/hello` avec header `Authorization: Bearer <token>` (appel autorisé).
- `img.png` : réponse 200 JSON de `/api/hello` confirmant l’accès protégé.

## Comment rejouer les tests
1. Lancer l’application : `mvn spring-boot:run`.
2. Obtenir un token :  
   ```
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Accept: application/json" \
     -H "Content-Type: application/json" \
     -d '{"username":"user","password":"password"}'
   ```
3. Appeler l’endpoint protégé :  
   ```
   curl http://localhost:8080/api/hello \
     -H "Authorization: Bearer <token_reçu>"
   ```
4. Vérifier qu’un appel sans token à `/api/hello` renvoie 401/403.

## Points clés
- Authentification stateless via JWT, pas de session serveur.
- Utilisateurs en mémoire pour la démo.
- Le filtre JWT est inséré avant l’authentification username/password standard.
