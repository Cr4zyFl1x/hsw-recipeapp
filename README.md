## API Key
Für eine funktionierende Wetter-Abfrage, muss ein persönlicher API-Key hinzugefügt werden.

Schritt 1: Erstelle einen API-Key auf der Website von [OpenWeather](https://home.openweathermap.org/api_keys).  

Schritt 2: Kopiere den Key und füge ihn in die local.properties folgendermaßen ein:
```bash
API_KEY="hier den Key einfügen"
```

Schritt 3: Gehe in die build.gradle.kts (Module:app). Ggf. musst du noch unter buildFeatures 
```bash
buildConfig = true
```
hinzufügen. Es sollte dann folgendermaßen aussehen:
```bash
buildFeatures {
        viewBinding = true
        buildConfig = true
}
```
Schritt 4: Lasse das Projekt einmal synchroniseren und baue es dann neu. Nun sollte deine eine BuildConfig.java aktualisiert worden sein und du kannst deinen API-Key dort finden.
