## STEP: Build
FROM node:14-alpine as builder

COPY package.json package-lock.json nginx.conf ./

RUN npm set progress=false && npm config set depth 0 && npm cache clean --force
RUN npm install && mkdir /app && cp -R ./node_modules ./app

WORKDIR /app

COPY . .

RUN npm run build

## STEP: Nginx
FROM nginx:1.15.9-alpine

RUN apk update && apk upgrade

RUN rm /etc/nginx/nginx.conf
COPY nginx.conf /etc/nginx/

# Copy Build files
RUN rm -rf /usr/share/nginx/html/*
COPY --from=builder /app/dist/web /usr/share/nginx/html

# Install the Agent
RUN apk add jq

# Ports
EXPOSE 8080

# Support running as arbitrary user which belogs to the root group
RUN chmod -R g+rwx /var/cache/nginx /var/run /var/log/nginx /usr/share/nginx /etc/nginx
RUN chgrp -R root /var/cache/nginx

RUN addgroup nginx root
USER nginx

# Startup command
# ENTRYPOINT "nginx -g 'daemon off;'"
CMD ["nginx", "-g", "daemon off;"]
