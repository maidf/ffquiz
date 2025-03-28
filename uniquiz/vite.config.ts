import { defineConfig } from "vite"
import uni from "@dcloudio/vite-plugin-uni"

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [uni()],
    server: {
        host: "localhost",
        port: 5173,
        proxy: {
            '/api': {
                target: 'http://localhost:9901', // 后端地址
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, ''),
            }
        },
    },
})
