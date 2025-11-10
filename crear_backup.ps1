# ============================================
# SCRIPT PARA CREAR BACKUP DE LA BASE DE DATOS
# ============================================
# Uso: .\crear_backup.ps1
# 
# Este script crea un backup completo de tu base de datos
# con fecha y hora para tener historial de versiones
# ============================================

Write-Host "Creando backup de la base de datos..." -ForegroundColor Cyan

# Obtener fecha y hora actual para el nombre del archivo
$fecha = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
$archivoBackup = "backup_db_foodix_$fecha.sql"

# Crear el backup
Write-Host "Exportando base de datos..." -ForegroundColor Yellow
docker exec -i sistemafoodix-db-1 mysqldump -u root -p'root' db_foodix > $archivoBackup

# Verificar que se creo correctamente
if (Test-Path $archivoBackup) {
    $tamano = (Get-Item $archivoBackup).Length / 1KB
    Write-Host "✅ Backup creado exitosamente: $archivoBackup" -ForegroundColor Green
    Write-Host "Tamano: $([math]::Round($tamano, 2)) KB" -ForegroundColor Green
    
    # Copiar como backup_db_foodix.sql (para que los companeros siempre tengan el ultimo)
    Copy-Item $archivoBackup "backup_db_foodix.sql" -Force
    Write-Host "✅ Actualizado: backup_db_foodix.sql" -ForegroundColor Green
    
    Write-Host ""
    Write-Host "Proximos pasos:" -ForegroundColor Cyan
    Write-Host "1. Haz commit del backup:" -ForegroundColor White
    Write-Host "   git add backup_db_foodix.sql" -ForegroundColor Gray
    Write-Host "   git commit -m 'chore: Actualizar backup de base de datos'" -ForegroundColor Gray
    Write-Host "   git push origin main" -ForegroundColor Gray
    Write-Host ""
    Write-Host "2. (Opcional) Conserva el backup con fecha por seguridad:" -ForegroundColor White
    Write-Host "   $archivoBackup" -ForegroundColor Gray
    
} else {
    Write-Host "❌ Error: No se pudo crear el backup" -ForegroundColor Red
    Write-Host "Verifica que Docker este corriendo: docker ps" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Proceso completado" -ForegroundColor Cyan
