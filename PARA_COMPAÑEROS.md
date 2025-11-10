# 🚀 Guía Rápida para el Equipo

## 📌 Cuando Daniela actualice el backup

### Solo necesitas 2 comandos:

#### **En PowerShell:**
```powershell
# 1. Obtener el backup actualizado
git pull

# 2. Actualizar tu base de datos
.\importar_backup.ps1
```

#### **En Git Bash:**
```bash
# 1. Obtener el backup actualizado
git pull

# 2. Actualizar la BD
powershell.exe -File importar_backup.ps1
```

Escribe `S` cuando pregunte y **¡listo!** 🎉

---

## 🔄 ¿Qué hace esto?

1. **`git pull`** → Descarga el archivo `backup_db_foodix.sql` actualizado
2. **`.\importar_backup.ps1`** → Actualiza tu BD local con los datos de Daniela

**Tu Docker NO se reinicia**, solo se actualiza la base de datos.

---

## ⏱️ ¿Cuánto tarda?

**20-30 segundos en total.**

---

## 📋 Ejemplo completo

```powershell
# Ver que Docker esté corriendo
PS> docker ps
CONTAINER ID   IMAGE                    STATUS
abc123         sistemafoodix-mi-app     Up 2 hours
def456         mysql:8.0                Up 2 hours

# Obtener cambios
PS> git pull
Updating a1b2c3d..e4f5g6h
Fast-forward
 backup_db_foodix.sql | 150 ++++++++++++++++++++++++++++++++++++
 1 file changed, 150 insertions(+)

# Actualizar BD
PS> .\importar_backup.ps1

🔄 Actualizando base de datos...

📦 Backup encontrado: 45.2 KB

⚠️  Esto actualizará tu BD con los cambios de Daniela
¿Continuar? (S/N): S

📥 Importando cambios...
✅ Base de datos actualizada

📊 Usuarios: 8 | Restaurantes: 5

🔄 Reiniciando aplicación...
✅ ¡Listo! La BD está sincronizada
🌐 Accede a: http://localhost:8080
```

---

## ❌ Si algo falla

### Error: "Docker no está corriendo"
```powershell
# Abrir Docker Desktop y esperar 30 segundos
# Luego verificar:
docker ps
```

### Error: "No se encontró el archivo backup"
```powershell
# Asegúrate de haber hecho git pull
git pull

# Verifica que el archivo existe
Get-ChildItem backup_db_foodix.sql
```

### No veo los cambios
```powershell
# Limpia caché del navegador
# En Chrome/Edge: Ctrl + F5

# O reinicia la app
docker-compose restart mi-app
```

---

## 💡 Comunicación con el equipo

### Daniela debería avisar:
> "Actualicé el backup con 3 restaurantes nuevos. Hagan: `git pull` + `.\importar_backup.ps1`"

### Tú respondes:
> "Listo, ya actualicé mi BD ✅"

---

## ⚙️ ¿Qué NO debes hacer?

- ❌ **NO** hacer `docker-compose down` (perdería tu BD)
- ❌ **NO** ejecutar `datos_iniciales.sql` (es solo para setup inicial)
- ❌ **NO** modificar manualmente la BD sin avisar al equipo

---

## 📚 Más información

Si quieres entender todo el proceso en detalle, lee:
- **`GUIA_BACKUP_RESTORE.md`** - Guía completa con ejemplos
- **`INSTRUCCIONES_SETUP.md`** - Setup inicial del proyecto

---

## 🆘 ¿Dudas?

Pregunta en el grupo o revisa los logs:
```powershell
# Ver logs de la BD
docker logs sistemafoodix-db-1 --tail 50

# Ver logs de la aplicación
docker logs sistemafoodix-mi-app-1 --tail 50
```

---

> **🎯 Recuerda:** Solo 2 comandos → `git pull` + `.\importar_backup.ps1` ✅
