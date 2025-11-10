# 📦 Guía de Backup y Restore - FooDix

## 🎯 ¿Cuándo usar cada script?

### **TÚ (Daniela) - Crear backups**
Cada vez que hagas cambios importantes en la base de datos:
- ✅ Registraste nuevos usuarios
- ✅ Aprobaste restaurantes
- ✅ Agregaste datos de prueba
- ✅ Modificaste información existente

**🔧 Script:** `crear_backup.ps1`

---

### **TUS COMPAÑEROS - Actualizar su BD**
Cuando necesiten sincronizar con tus cambios:
- ✅ Hiciste push con nuevo backup
- ✅ Quieren ver tus datos recientes
- ✅ Necesitan estar al día con tu versión

**🔧 Script:** `importar_backup.ps1`

> ⚠️ **Importante:** Tus compañeros YA TIENEN Docker corriendo, solo necesitan actualizar la BD

---

## 📝 PASO A PASO: TÚ (Crear backup)

### 1️⃣ Cuando quieras compartir tu BD actual

```powershell
# 1. Abre PowerShell en la carpeta del proyecto
cd C:\Users\DANIELA\OneDrive\Documentos\Integrador-Foodix\SistemaFooDix

# 2. Ejecuta el script
.\crear_backup.ps1
```

**Resultado:**
```
🔄 Creando backup de la base de datos...
✅ Backup creado exitosamente: backup_20250110_143022.sql (41.8 KB)
```

### 2️⃣ Comparte el archivo con tus compañeros

**Opción A - GitHub:**
```powershell
git add backup_db_foodix.sql
git commit -m "feat: actualizar backup de BD con nuevos restaurantes"
git push
```

**Opción B - Google Drive / WhatsApp:**
- Envía el archivo `backup_20250110_143022.sql`
- O sobrescribe `backup_db_foodix.sql` y compártelo

---

## 📥 PASO A PASO: TUS COMPAÑEROS (Actualizar BD)

### 1️⃣ Obtener tu backup actualizado

```powershell
# Ir a la carpeta del proyecto
cd C:\ruta\al\proyecto\SistemaFooDix

# Obtener tus cambios
git pull
```

### 2️⃣ Verificar que Docker esté corriendo

```powershell
# Ver contenedores activos
docker ps
```

Deberías ver: `sistemafoodix-db-1` y `sistemafoodix-mi-app-1`

### 3️⃣ Ejecutar el script de actualización

```powershell
.\importar_backup.ps1
```

**Se verá así:**
```
� Actualizando base de datos...

� Backup encontrado: 41.8 KB

⚠️  Esto actualizará tu BD con los cambios de Daniela
¿Continuar? (S/N): S

📥 Importando cambios...
✅ Base de datos actualizada

📊 Usuarios: 5 | Restaurantes: 2

🔄 Reiniciando aplicación...
✅ ¡Listo! La BD está sincronizada
🌐 Accede a: http://localhost:8080
```

### 4️⃣ Verificar

1. Ir a: http://localhost:8080
2. Ver los nuevos datos que Daniela agregó

---

## 🔄 Flujo de trabajo del equipo

```
┌─────────────────────────────────────┐
│   TÚ (Daniela)                      │
│                                     │
│ 1. Hacer cambios en la BD          │
│    (registrar usuarios,             │
│     aprobar restaurantes, etc.)     │
│                                     │
│ 2. .\crear_backup.ps1              │◄─── Cuando termines tus cambios
│    ✅ backup_db_foodix.sql          │
│                                     │
│ 3. git add backup_db_foodix.sql    │
│    git commit -m "actualizar BD"    │
│    git push                         │
└──────────────┬──────────────────────┘
               │
               │ GitHub actualizado
               │
               ▼
┌─────────────────────────────────────┐
│   TUS COMPAÑEROS                    │
│   (Ya tienen Docker corriendo)      │
│                                     │
│ 1. git pull                         │◄─── Obtienen tu backup
│                                     │
│ 2. .\importar_backup.ps1           │◄─── Solo 1 comando
│    Escriben: S                      │
│                                     │
│ 3. ¡Listo! 🎉                       │     BD sincronizada
│    BD actualizada automáticamente   │
└─────────────────────────────────────┘
```

**⏱️ Tiempo total para tus compañeros: 30 segundos**

---

## ⚠️ Preguntas frecuentes

### ❓ ¿Mis compañeros necesitan bajar y subir Docker cada vez?

**NO.** Solo necesitan:
1. `git pull` (obtener tu backup)
2. `.\importar_backup.ps1` (actualizar BD)

Docker sigue corriendo, solo se actualiza la base de datos.

---

### ❓ ¿Con qué frecuencia debo crear backups?

**Recomendado:**
- ✅ Después de registrar datos de prueba importantes
- ✅ Después de aprobar restaurantes
- ✅ Antes de cada reunión de equipo
- ✅ Al final de tu sesión de trabajo

**Regla simple:** Si tus compañeros necesitan ver lo que hiciste, crea un backup.

---

### ❓ ¿El backup sobrescribe todo?

**SÍ**, pero es rápido y seguro. El script pide confirmación:
```
⚠️  Esto actualizará tu BD con los cambios de Daniela
¿Continuar? (S/N):
```

Si alguien tenía datos locales que no quiere perder, debe decir "N" y avisarte.

---

### ❓ ¿Qué pasa si olvido hacer backup?

Tus compañeros trabajarán con datos viejos. Cuando lo notes:
1. Haz el backup: `.\crear_backup.ps1`
2. Avisa al equipo: "Hice push del backup actualizado"
3. Ellos hacen: `git pull` + `.\importar_backup.ps1`

---

### ❓ ¿Funciona en Mac/Linux?

Los scripts son para Windows PowerShell. En Mac/Linux, tus compañeros pueden hacer:
```bash
# Actualizar
git pull

# Importar manualmente
docker exec -i sistemafoodix-db-1 mysql -u root -p'root' db_foodix < backup_db_foodix.sql

# Reiniciar app
docker-compose restart mi-app
```

---

## 🛠️ Solución de problemas

### Error: "Docker no está corriendo"

```powershell
# Ver si Docker está activo
docker ps

# Si no responde, iniciar Docker Desktop y esperar 30 segundos
# Luego verificar de nuevo
docker ps
```

---

### Error: "No se encontró el archivo backup"

```powershell
# 1. Verificar que hiciste git pull
git pull

# 2. Verificar que el archivo existe
Get-ChildItem backup_db_foodix.sql

# 3. Verificar que estás en la carpeta correcta
Get-Location
# Debe mostrar: ...\SistemaFooDix
```

---

### Error: La BD no se actualizó

```powershell
# Reiniciar Docker completo
docker-compose restart

# Esperar 15 segundos y probar de nuevo
.\importar_backup.ps1
```

---

### No veo los cambios en el navegador

```powershell
# 1. Limpiar caché del navegador (Ctrl + F5)

# 2. Verificar datos en la BD
docker exec -i sistemafoodix-db-1 mysql -u root -p'root' db_foodix -e "SELECT * FROM restaurante;"

# 3. Reiniciar la app
docker-compose restart mi-app
```

---

## 📚 Archivos relacionados

- `crear_backup.ps1` - Script para TI (crear backups)
- `importar_backup.ps1` - Script para TUS COMPAÑEROS (importar)
- `backup_db_foodix.sql` - El backup actual (41.8 KB)
- `datos_iniciales.sql` - Estructura inicial (alternativa)
- `GUIA_IMPORTAR_DB.md` - Guía manual (sin scripts)

---

## ✅ Checklist para compartir con el equipo

### **Para TI (Daniela):**

Cuando hagas cambios importantes:

- [ ] Hiciste cambios en la BD (registros, aprobaciones, etc.)
- [ ] Ejecutaste: `.\crear_backup.ps1`
- [ ] Viste el mensaje: "✅ Backup creado exitosamente"
- [ ] Hiciste: `git add backup_db_foodix.sql`
- [ ] Hiciste: `git commit -m "actualizar BD con [tus cambios]"`
- [ ] Hiciste: `git push`
- [ ] **Avisaste al equipo:** "Actualicé el backup, hagan git pull + importar_backup.ps1"

---

### **Para TUS COMPAÑEROS:**

Cuando Daniela avise que actualizó el backup:

- [ ] Docker está corriendo (verificar: `docker ps`)
- [ ] Ejecutaste: `git pull`
- [ ] Viste: "backup_db_foodix.sql actualizado"
- [ ] Ejecutaste: `.\importar_backup.ps1`
- [ ] Escribiste: `S` para confirmar
- [ ] Viste: "✅ Base de datos actualizada"
- [ ] Refrescaste el navegador: http://localhost:8080
- [ ] ¡Ves los nuevos datos de Daniela! ✅

**⏱️ Tiempo total: 30 segundos**

---

## 🎓 Ejemplo de uso real

**Escenario:** Registraste 3 restaurantes nuevos y aprobaste 2. Quieres que tu equipo los vea.

### **TÚ haces:**
```powershell
# 1. Crear backup con los nuevos restaurantes
.\crear_backup.ps1

# ✅ Resultado:
# Backup creado: backup_db_foodix.sql (45.2 KB)

# 2. Subir a GitHub
git add backup_db_foodix.sql
git commit -m "feat: agregar 3 restaurantes + aprobar 2"
git push
```

### **TUS COMPAÑEROS hacen:**
```powershell
# 1. Obtener tu backup
git pull

# 2. Actualizar su BD (¡solo esto!)
.\importar_backup.ps1
# Escribir: S

# ✅ Resultado:
# Base de datos actualizada
# Usuarios: 8 | Restaurantes: 5
# ¡Listo! La BD está sincronizada

# 3. Refrescar el navegador
# Ahora ven los 3 restaurantes nuevos y los 2 aprobados
```

**⏱️ Tiempo para tus compañeros: 20 segundos**

---

## 📞 Ayuda

Si tus compañeros tienen problemas:

1. **Verificar Docker:** `docker ps` debe mostrar 2 contenedores
2. **Ver logs:** `docker logs sistemafoodix-db-1 --tail 50`
3. **Reiniciar todo:** `docker-compose down` y `docker-compose up -d`
4. **Verificar archivo:** El backup debe pesar ~42 KB

---

> **💡 Tip:** Crea un backup antes de hacer cualquier cambio arriesgado. ¡Es como un "Ctrl+Z" para tu base de datos!
