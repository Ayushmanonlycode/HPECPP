FROM nginx:alpine

COPY api-gateway/src/main/api-gateway.conf /etc/nginx/nginx.conf

EXPOSE 9080

CMD ["nginx", "-g", "daemon off;"]
