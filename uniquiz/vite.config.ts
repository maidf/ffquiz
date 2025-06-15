import { defineConfig } from "vite"
import uni from "@dcloudio/vite-plugin-uni"

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [uni()],
    server: {
        host: "127.0.0.1",
        port: 5173,
        proxy: {
            '/api': {
                target: 'http://localhost:10032', // 后端地址
                changeOrigin: true,
                rewrite: (path: any) => path.replace(/^\/api/, ''),
            }
        },
    },
})
