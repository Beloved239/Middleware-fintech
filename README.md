# Fintech Middleware Solution

A comprehensive microservices-based fintech middleware platform built with Spring Boot and Spring Cloud, providing secure authentication, customer management, and third-party integrations.

## 🏗️ Architecture Overview 

This solution implements a distributed microservices architecture with the following core components:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Config Server │    │  Eureka Server  │    │ Gateway Server  │
│     :8071       │    │     :8070       │    │     :8072       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Auth Service   │    │Customer Service │    │Third-Party Svc  │
│     :8081       │    │     :8080       │    │     :8082       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🚀 Services Overview

### Core Infrastructure Services

| Service | Port | Purpose | Dependencies |
|---------|------|---------|--------------|
| **Config Server** | 8071 | Centralized configuration management | None |
| **Eureka Server** | 8070 | Service discovery and registration | Config Server |
| **Gateway Server** | 8072 | API Gateway and routing | Config + Eureka |

### Business Services

| Service | Port | Purpose | Key Features |
|---------|------|---------|--------------|
| **Auth Service** | 8081 | Authentication & Authorization | JWT tokens, User validation, Role management |
| **Customer Service** | 8080 | Customer management | Account creation, Profile management, KYC |
| **Third-Party Service** | 8082 | External integrations | Payment gateways, Credit bureaus, Banking APIs |

## 🛠️ Prerequisites

- **Java**: 17 or higher
- **Maven**: 3.8+
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code
- **Git**: For version control
- **Postman**: For API testing (optional)

## 🚦 Quick Start Guide

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd fintech-middleware-solution
```

### Step 2: Start Services in Order

**⚠️ IMPORTANT**: Services must be started in the exact order below to ensure proper dependency resolution.

#### 1. Start Config Server (Port 8071)

```bash
# Navigate to config server directory
cd configserver

# Start the service
mvn spring-boot:run

# Or using jar
mvn clean package
java -jar target/configserver-1.0.0.jar

# Verify it's running
curl http://localhost:8071/actuator/health
```

**Wait for**: `Started ConfigserverApplication` log message

#### 2. Start Eureka Server (Port 8070)

```bash
# Navigate to eureka server directory
cd ../eurekaserver

# Start the service
mvn spring-boot:run

# Or using jar
mvn clean package
java -jar target/eurekaserver-1.0.0.jar

# Verify it's running
curl http://localhost:8070/actuator/health

# Check Eureka dashboard
open http://localhost:8070
```

**Wait for**: `Started EurekaserverApplication` log message

#### 3. Start Gateway Server (Port 8072)

```bash
# Navigate to gateway server directory
cd ../gatewayserver

# Start the service
mvn spring-boot:run

# Or using jar
mvn clean package
java -jar target/gatewayserver-1.0.0.jar

# Verify it's running
curl http://localhost:8072/actuator/health

# Check gateway routes
curl http://localhost:8072/actuator/gateway/routes
```

**Wait for**: Gateway registration in Eureka dashboard

#### 4. Start Business Services (Any Order)

**Auth Service (Port 8081):**
```bash
cd ../auth-service
mvn spring-boot:run

# Verify
curl http://localhost:8081/actuator/health
```

**Customer Service (Port 8080):**
```bash
cd ../customer-service
mvn spring-boot:run

# Verify
curl http://localhost:8080/actuator/health
```

**Third-Party Service (Port 8082):**
```bash
cd ../third-party-service
mvn spring-boot:run

# Verify
curl http://localhost:8082/actuator/health
```

## 🔍 Verification Steps

### 1. Check Eureka Dashboard
Visit `http://localhost:8070` and verify all services are registered:
- ✅ GATEWAYSERVER
- ✅ AUTH-SERVICE
- ✅ CUSTOMER-SERVICE (or USER-SERVICE)
- ✅ THIRD-PARTY-SERVICE

### 2. Test Gateway Routes
```bash
# View all routes
curl http://localhost:8072/actuator/gateway/routes

# Test service routing
curl http://localhost:8072/AUTH-SERVICE/actuator/health
curl http://localhost:8072/CUSTOMER-SERVICE/actuator/health
curl http://localhost:8072/THIRD-PARTY-SERVICE/actuator/health
```

### 3. Test Direct Service Access
```bash
# Direct service calls (for debugging)
curl http://localhost:8081/actuator/health  # Auth Service
curl http://localhost:8080/actuator/health  # Customer Service  
curl http://localhost:8082/actuator/health  # Third-Party Service
```

## 🔧 Configuration Management

### Environment-Specific Configurations

The Config Server manages configurations for different environments:

```
config-repo/
├── application.yml                    # Common properties
├── gatewayserver.yml                 # Gateway-specific config
├── gatewayserver-dev.yml             # Gateway dev environment
├── gatewayserver-prod.yml            # Gateway prod environment
├── auth-service.yml                  # Auth service config
├── customer-service.yml              # Customer service config
└── third-party-service.yml           # Third-party service config
```

### Updating Configuration
1. Update configuration files in the config repository
2. Restart Config Server
3. Refresh services: `POST http://localhost:PORT/actuator/refresh`

## 🌐 API Gateway Usage

### Service Routing Pattern
```
http://localhost:8072/{SERVICE-NAME}/{endpoint}
```

### Example API Calls

**Authentication Service:**
```bash
# User login
POST http://localhost:8072/AUTH-SERVICE/auth/login
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123"
}

# Validate token
GET http://localhost:8072/AUTH-SERVICE/auth/validate
Authorization: Bearer <jwt-token>
```

**Customer Service:**
```bash
# Get customer profile
GET http://localhost:8072/CUSTOMER-SERVICE/api/v1/customers/{customerId}
Authorization: Bearer <jwt-token>

# Create customer
POST http://localhost:8072/CUSTOMER-SERVICE/api/v1/customers
Content-Type: application/json
Authorization: Bearer <jwt-token>

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890"
}
```

**Third-Party Service:**
```bash
# Process payment
POST http://localhost:8072/THIRD-PARTY-SERVICE/api/v1/payments/process
Content-Type: application/json
Authorization: Bearer <jwt-token>

{
  "amount": 1000.00,
  "currency": "USD",
  "paymentMethod": "CARD"
}

# Credit check
GET http://localhost:8072/THIRD-PARTY-SERVICE/api/v1/credit-check/{customerId}
Authorization: Bearer <jwt-token>
```

## 🐛 Troubleshooting

### Common Issues and Solutions

#### 1. Service Registration Failed
**Problem**: Service not appearing in Eureka dashboard
**Solution**:
```bash
# Check Eureka client configuration
curl http://localhost:PORT/actuator/env | grep eureka

# Verify Eureka server is running
curl http://localhost:8070/actuator/health
```

#### 2. Gateway Route Not Found
**Problem**: 503 Service Unavailable error
**Solution**:
```bash
# Check if service is registered
curl http://localhost:8070/eureka/apps

# Verify gateway routes
curl http://localhost:8072/actuator/gateway/routes

# Check load balancer configuration
curl http://localhost:8072/actuator/beans | grep -i loadbalancer
```

#### 3. Config Server Connection Issues
**Problem**: Service can't fetch configuration
**Solution**:
```bash
# Test config server
curl http://localhost:8071/actuator/health

# Check specific service config
curl http://localhost:8071/auth-service/default
```

#### 4. Port Already in Use
**Problem**: `Port 8070 is already in use`
**Solution**:
```bash
# Kill process using the port
lsof -ti:8070 | xargs kill -9

# Or use different port in application.yml
server:
  port: 8071
```

### Debug Logging
Enable debug logging by adding to `application.yml`:
```yaml
logging:
  level:
    com.netflix.eureka: DEBUG
    com.netflix.discovery: DEBUG
    org.springframework.cloud.gateway: DEBUG
    root: INFO
```

## 📊 Monitoring and Management

### Health Endpoints
- **Config Server**: `http://localhost:8071/actuator/health`
- **Eureka Server**: `http://localhost:8070/actuator/health`
- **Gateway Server**: `http://localhost:8072/actuator/health`
- **Auth Service**: `http://localhost:8081/actuator/health`
- **Customer Service**: `http://localhost:8080/actuator/health`
- **Third-Party Service**: `http://localhost:8082/actuator/health`

### Management Endpoints
```bash
# Gateway routes
GET http://localhost:8072/actuator/gateway/routes

# Service registry
GET http://localhost:8070/eureka/apps

# Configuration
GET http://localhost:8071/{service-name}/default

# Metrics
GET http://localhost:PORT/actuator/metrics

# Environment
GET http://localhost:PORT/actuator/env
```

## 🔒 Security Features

### Authentication Flow
1. **Login Request** → Auth Service validates credentials
2. **JWT Token** → Generated and returned to client
3. **Token Validation** → Gateway validates token for protected routes
4. **Service Authorization** → Each service validates specific permissions

### Security Headers
The Gateway automatically adds security headers:
- `X-Frame-Options: DENY`
- `X-Content-Type-Options: nosniff`
- `X-XSS-Protection: 1; mode=block`

## 🚀 Deployment

### Development Environment
```bash
# Start all services locally
./start-all-services.sh

# Stop all services
./stop-all-services.sh
```

### Production Deployment
```bash
# Build all services
mvn clean package -DskipTests

# Deploy with Docker
docker-compose up -d

# Or deploy to cloud platform
kubectl apply -f k8s/
```

## 📈 Performance Considerations

### Recommended JVM Settings
```bash
# For each service
-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200
```

### Connection Pool Settings
```yaml
# Database connections
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/new-feature`
3. Commit changes: `git commit -am 'Add new feature'`
4. Push to branch: `git push origin feature/new-feature`
5. Submit Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:
- **Email**: support@fintech-middleware.com
- **Documentation**: [Wiki](https://github.com/your-org/fintech-middleware/wiki)
- **Issues**: [GitHub Issues](https://github.com/your-org/fintech-middleware/issues)

---

**Happy Coding! 🚀**
