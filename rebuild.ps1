Write-Host "🔄 Compilando app..."
./mvnw clean package -DskipTests

Write-Host "🐳 Reconstruyendo imagen y levantando servicios..."
docker-compose down
docker-compose up --build
