
FROM oven/bun:latest as builder

WORKDIR /app
COPY . .

RUN bun i
RUN bun build:h5

FROM nginx:alpine
COPY --from=builder /app/dist/build/h5 /app/html

EXPOSE 80

CMD [ "nginx", "-g", "daemon off;" ]