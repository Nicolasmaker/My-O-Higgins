# ============================================================
# Frontend (React + Vite) — build estático servido por Nginx
# ============================================================

# ---- Build ----
FROM node:22-alpine AS build
WORKDIR /app

# Cachea dependencias: solo se reinstalan si cambia el lockfile
COPY package.json package-lock.json ./
RUN npm ci

# Compila el build de producción a /app/dist
COPY . .
RUN npm run build

# ---- Serve ----
FROM nginx:1.27-alpine
# Reemplaza la config default: SPA + reverse proxy a los microservicios
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
