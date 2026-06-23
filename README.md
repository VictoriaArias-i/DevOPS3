# DevOPS3 - Microservicio Veterinaria Cliente

[![CI/CD](https://github.com/VictoriaArias-i/DevOPS3/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/VictoriaArias-i/DevOPS3/actions/workflows/ci-cd.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=VictoriaArias-i_DevOPS3&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=VictoriaArias-i_DevOPS3)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=VictoriaArias-i_DevOPS3&metric=bugs)](https://sonarcloud.io/summary/new_code?id=VictoriaArias-i_DevOPS3)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=VictoriaArias-i_DevOPS3&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=VictoriaArias-i_DevOPS3)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=VictoriaArias-i_DevOPS3&metric=coverage)](https://sonarcloud.io/summary/new_code?id=VictoriaArias-i_DevOPS3)

Microservicio REST para la gestión de clientes del sistema de veterinaria. Desarrollado con Spring Boot 3 y desplegado en Kubernetes sobre AWS EKS.

---

## Tecnologías

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.2.5 |
| Base de datos | MySQL 8.0 |
| Migraciones | Flyway |
| Seguridad | Spring Security + JWT |
| Contenedor | Docker |
| Orquestación | Kubernetes (AWS EKS) |
| CI/CD | GitHub Actions |
| Calidad de código | SonarQube (SonarCloud) |
| Seguridad de dependencias | Snyk |
| Cobertura de tests | JaCoCo (mínimo 60%) |
| Métricas | Spring Actuator + Prometheus |
| Monitoreo | AWS CloudWatch |

---

## API REST

### Autenticación

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/auth/login` | Genera token JWT | No |

**Body login:**
```json
{
  "username": "admin",
  "password": "password"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer"
}
```

### Clientes

Todos los endpoints requieren el header: `Authorization: Bearer <token>`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/clientes` | Lista todos los clientes |
| GET | `/api/v1/clientes/{id}` | Busca cliente por ID |
| GET | `/api/v1/clientes/rut/{rut}` | Busca cliente por RUT |
| POST | `/api/v1/clientes` | Crea un nuevo cliente |
| PUT | `/api/v1/clientes/{id}` | Actualiza un cliente |
| DELETE | `/api/v1/clientes/{id}` | Elimina un cliente |

**Body cliente (POST/PUT):**
```json
{
  "nombre": "Juan Pérez González",
  "rut": "12345678-9",
  "telefono": "+56912345678",
  "email": "juan.perez@email.com",
  "direccion": "Av. Providencia 1234, Santiago",
  "fechaRegistro": "2024-01-10"
}
```

### Métricas y salud

| Endpoint | Descripción |
|----------|-------------|
| `/actuator/health` | Estado de la aplicación |
| `/actuator/prometheus` | Métricas para Prometheus |

---

## Ejecutar localmente

### Requisitos
- Docker Desktop
- Java 21
- Maven 3.9+

### Con Docker Compose

```bash
# Clonar el repositorio
git clone https://github.com/VictoriaArias-i/DevOPS3.git
cd DevOPS3

# Levantar la aplicación y MySQL
docker compose up -d

# Verificar que está corriendo
curl http://localhost:8080/actuator/health
```

### Con Maven

```bash
# Requiere MySQL corriendo en localhost:3306
mvn spring-boot:run
```

---

## Pipeline CI/CD

El pipeline se ejecuta automáticamente en cada push a `main` o `develop`.

```
push/PR
   │
   ├─► test         → Compila, ejecuta tests, valida cobertura JaCoCo ≥ 60%
   │
   ├─► sonarqube    → Analiza calidad del código (falla si Quality Gate no pasa)
   │
   ├─► security     → Escanea vulnerabilidades con Snyk (falla si detecta HIGH/CRITICAL)
   │
   └─► docker       → Construye y publica imagen en GitHub Container Registry
          │
          └─► deploy → Despliega con Docker Compose y verifica disponibilidad
```

### Secrets requeridos en GitHub

| Secret | Descripción |
|--------|-------------|
| `SONAR_TOKEN` | Token de SonarCloud |
| `SNYK_TOKEN` | Token de Snyk |
| `DB_PASSWORD` | Contraseña de MySQL |
| `JWT_SECRET` | Clave secreta para firmar tokens JWT |

---

## Despliegue en Kubernetes (AWS EKS)

### Estructura de manifests

```
k8s/
├── namespace.yml          # Namespace: veterinaria
├── rbac.yml               # ServiceAccount y permisos
├── secret.yml             # Credenciales sensibles
├── configmap.yml          # Configuración de la app
├── storage-class.yml      # Disco SSD gp3 en AWS
├── mysql-pvc.yml          # Almacenamiento persistente MySQL
├── mysql-deployment.yml   # Deployment + Service de MySQL
├── deployment.yml         # Deployment de la app
├── service.yml            # Service LoadBalancer (puerto 80)
├── hpa.yml                # Autoescalado (1-4 réplicas, CPU > 70%)
├── network-policy.yml     # Políticas de red entre pods
├── ingress.yml            # Enrutamiento HTTP
├── cloudwatch-agent.yml   # Monitoreo con AWS CloudWatch
└── deploy.sh              # Script de despliegue
```

### Desplegar

```bash
# 1. Crear clúster EKS
eksctl create cluster \
  --name veterinaria-cluster \
  --region us-east-1 \
  --node-type t3.medium \
  --nodes 2

# 2. Conectar kubectl al clúster
aws eks update-kubeconfig --name veterinaria-cluster --region us-east-1

# 3. Aplicar todos los manifests en orden
bash k8s/deploy.sh

# 4. Obtener URL pública
kubectl get service veterinaria-cliente-service -n veterinaria
```

### Eliminar el clúster

```bash
eksctl delete cluster --name veterinaria-cluster --region us-east-1
```

---

## Monitoreo

### AWS CloudWatch
El agente de CloudWatch se despliega automáticamente como DaemonSet y recolecta:
- CPU y memoria por pod
- Logs de contenedores
- Métricas del clúster EKS

Disponible en: **AWS Console → CloudWatch → Container Insights**

### Prometheus
Métricas expuestas en `/actuator/prometheus`, incluyendo:
- Peticiones HTTP (count, latencia)
- Estado de la JVM
- Conexiones a base de datos
