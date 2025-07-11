# Build stage
FROM node:20-alpine AS build
WORKDIR /app

ARG VITE_BACKEND_HOST
ENV VITE_BACKEND_HOST=$VITE_BACKEND_HOST

COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build

# Serve stage
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

RUN rm -rf ./*

COPY --from=build /app/dist .  
COPY nginx.conf /etc/nginx/conf.d/default.conf 
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]