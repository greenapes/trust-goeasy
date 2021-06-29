# GOEASY Library Project #
This deliverable is the first of a series of public reports providing a comprehensive overview of the GOEASY platform (GEP) and pilots reference architecture.
The process used for software architecture design is based on ISO/IEC/IEEE 42010:2011 “Systems and software engineering - Architecture description”. It implies a process that builds on a set of relevant architecture viewpoints. In this first release, the focus is on defining the following views:
1. The Functional view, describes the components, their functionality, and their interactions.
2. The Deployment view, describes how and where the system will be deployed and what dependencies exist, considering hardware requirements and physical constraints.
3. The Information view describes the data models and the data flow as well as the distribution. The viewpoint also defines how the distinction between fresh and latest values is achieved.

Finally, the pilot’s use cases are described which will be relevant for the GOEASY project. The purpose of these use cases is to clarify how the GOEASY platform will work and which components are relevant for the different tasks.

The following companies / organizations work on this project:

* LINKS (ISMB) -> Istituto Superiore Mario Boella -> Turín
* FIT (Fraunhoffer) -> Colonia
* Greenares (Startup de Smartcities) -> Florencia
* CNET (Startup) -> Estocolmo
* BQ
* Ayuntamiento de Torino

More information -> https://goeasyproject.eu/

***
## How do I initialize this library?
``` java
GoeasyLib.INSTANCE.init(mContext);
```

## API 1 (Trusted Device)
``` java
TrustedDevice trustedDevice = new TrustedDevice();
boolean trusted = trustedDevice.isTrustedDevice();
```

## API 2 (Cell Tower Validation)
``` java
CellTowerValidation.INSTANCE.validateCellTower(location1, new CellTowerCallback() {

    @Override
    public void onFailure(Throwable t) {
        /* Message could be
            LOCATION_EXCEPTION = "The location services are not enabled"
            API_EXCEPTION = "Problems with Mozilla API Services"
        */
    }

    @Override
    public void onCellTowerValidationResponse(boolean response) {
    }

});
```
