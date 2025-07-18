Write-Host "🔄 Compilando app..."
./mvnw clean package -DskipTests

Write-Host "🛑 Deteniendo contenedores (sin eliminar volúmenes)..."
docker-compose down --remove-orphans

Write-Host "🐳 Reconstruyendo imagen y levantando servicios..."
docker-compose up --build

