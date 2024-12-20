
# Proyecto de Esteganografía

## Descripción
Este proyecto implementa una herramienta de esteganografía utilizando Kotlin y Maven. Permite codificar y decodificar texto dentro de imágenes.

## Requisitos y Comandos de Instalación:

    1.java 
        Requisitos: JDK (Java Development Kit) 8 o superior.
        Instalación: Descargar e instalar el JDK de Oracle desde el sitio oficial.
        Comando de intalación para ubuntu:
            sudo apt update
            sudo apt install openjdk-11-jdk

    2. Maven
        Requisitos: Java debe de estar ya instalado. Maven 3.6.0 o superior.
        Instalación: Descargar e instalar Maven desde el sitio oficial.
        Comando de intalación para ubuntu:
            sudo apt install maven

    3. Kotest
        Requisitos: Java debe de estar ya instalado. Kotest 4.6.
        Instalación: Agregar la siguiente línea a tu archivo pom.xml:
        <dependency>
            <groupId>io.kotest</groupId>
            <artifactId>kotest-runner-junit5</artifactId>
            <version>5.0.0</version>
        </dependency>
        Este comando ya viene agregado en el pom.xml

    4. JLine
        Requisitos: Se instala como dependecia en el archivo de pom.xml de Maven
        Instalación: Agregar la siguiente línea a tu archivo pom.xml:
        <dependency>
            <groupId>org.jline</groupId>
            <artifactId>jline</artifactId>
            <version>3.21.0</version>
        </dependency>
        Este comando ya viene agregado en el pom.xml

    5. Kotlin
        Requisitos: Java debe de estar ya instalado. Kotlin 1.7.10
                    Tambien ya viene agregado en el pom.xml
        Instalación: Agregar la siguiente línea a tu archivo pom.xml:
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-stdlib</artifactId>
            <version>1.5.31</version>
        </dependency>
        Este comando ya viene agregado en el pom.xml

Asegúrate de tener Java y Maven instalados antes de trabajar con Kotest y las demás dependencias.
Las dependencias de Kotest, JLine y Kotlin se gestionan a través del archivo pom.xml de Maven, por lo que no necesitas instalarlas manualmente, solo asegúrate de incluir las entradas correspondientes.
Comprobación de Instalación
Después de instalar Java y Maven, puedes verificar que están correctamente instalados con los siguientes comandos:

Para Java:

java -version


Para Maven:

mvn -version

Esto debería mostrarte la versión instalada de cada herramienta,
confirmando que la instalación fue exitosa.

### Instalación

1. **Clona el Repositorio**:
   Abre una terminal y ejecuta el siguiente comando para clonar el repositorio:
   ```
   git clone <url-del-repositorio>
   cd <directorio-del-repositorio>
   ```

2. **Compilar el Proyecto**:
   Para compilar el proyecto, ejecuta el siguiente comando:
   ```
   mvn clean install
   ```
   Este comando realiza las siguientes acciones:
   - Limpia las compilaciones anteriores.
   - Compila el código fuente.
   - Ejecuta las pruebas definidas.
   - Empaqueta la aplicación en un archivo JAR que estará ubicado en el directorio `target`.

### Ejecutar el Proyecto

Para ejecutar la aplicación principal, usa el siguiente comando:

mvn install
```
java -jar target/steganography.jar
```
**Nota**: Asegúrate de reemplazar `main.kt` con la clase principal correspondiente si el nombre de la clase es diferente.

### Ejecutar las Pruebas

Para ejecutar las pruebas definidas en el proyecto, utiliza el siguiente comando:
```
mvn test
```

Si deseas generar un sitio web estático con varios informes sobre el proyecto, 
incluyendo información sobre las dependencias, plugins, y más ejecuta el siguiente comando:
```
mvn site
```
Los archivos HTML generados se encontrarán en el directorio target/site del proyecto.

## Uso

- Coloca tus imágenes en el directorio `src/main/resources/images`.
- Utiliza los archivos de texto proporcionados en el directorio `src/main/resources/text` para la codificación.
- También puedes usar cualquier otra imagen o archivo de texto, asegurándote de colocarlos en la carpeta `resources` para mayor comodidad.

## Notas Importantes

- Asegúrate de tener los permisos correctos para leer los archivos de imagen y escribir los archivos de salida.
- El proyecto está estructurado para separar efectivamente el código fuente, las pruebas y los recursos, lo que facilita su mantenimiento.

Para cualquier problema o contribuciones, consulta el repositorio del proyecto.
