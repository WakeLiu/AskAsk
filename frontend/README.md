
## Install

```
npm install
```

## Run

```
npm run start
```

visit http://localhost:3000, http://localhost/login for login


docker command

```
docker run -ti --rm -w /app -v "$PWD":/app -p 3000:3000 node npm run start -- --host 0.0.0.0
```

## Build

```
npm run build
```


docker command

```
docker run -ti --rm -w /app -v "$PWD":/app node npm run build
```
