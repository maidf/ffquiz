<template>
    <view class="content">

        <br>
        <uni-forms :modelValue="user">
            <uni-forms-item label="账号" name="account">
                <uni-easyinput disabled type="text" v-model="user.account" />
            </uni-forms-item>
            <uni-forms-item label="姓名" name="name">
                <uni-easyinput type="text" v-model="user.name" />
            </uni-forms-item>
            <uni-forms-item name="email" label="邮箱">
                <uni-easyinput type="text" v-model="user.email" />
            </uni-forms-item>
            <uni-forms-item label="身份" name="role">
                <uni-easyinput disabled type="text" v-model="user.role" />
            </uni-forms-item>
            <uni-forms-item label="创建时间" name="createTime">
                <uni-easyinput disabled type="text" v-model="user.createTime" />
            </uni-forms-item>
        </uni-forms>

        <button size="mini" @click="update_usr_msg(token)">
            更新
        </button>
    </view>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'

onMounted(() => {
    get_token()
    fetch_usr_msg(token.value)
})

interface usr {
    id: number | null
    account: string | null
    name: string | null
    email: string | null
    role: string | null
    createTime: Date | null
}

const user = ref<usr>({
    id: null,
    account: null,
    name: null,
    email: null,
    role: null,
    createTime: null
})

const get_token = () => {
    token.value = uni.getStorageSync(
        'Authorization'
    )

    if (!token.value) {
        uni.redirectTo({ url: "/pages/user/login" })
    }
}
const token = ref()


const update_usr_msg = (token: string) => {
    if (user.value.name == null) return
    if (user.value.email == null) return
    if (user.value.name.length < 1) {
        user.value.name = null
    }
    if (user.value.email.length < 1) {
        user.value.email = null
    }

    uni.request({
        url: "/api/usr/info",
        data: {
            name: user.value.name,
            email: user.value.email
        },
        method: "PUT",
        header: {
            'Authorization': token
        }
    }).then(res => {
        if (res.statusCode == 200) {
            alert(res.data)
            fetch_usr_msg(token)
        }
    }).catch(err => alert(err))
}

const fetch_usr_msg = (token: string) => {
    uni.request({
        url: "/api/usr/info",
        header: {
            'Authorization': token
        }
    }).then(res => {
        if (res.statusCode == 200) {
            user.value = res.data as usr
        } else {
            alert(res.data)
        }
    }).catch(err => alert(err))
}

</script>

<style lang="scss">
.content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

}

.content {
    .box1 {
        border: 2px solid red;
    }

    .box2 {
        border: 2px solid blue;
    }

    .box3 {
        width: 300px;
        height: 200px;
        border: 2px solid pink;
    }
}

uni-easyinput[disabled] {
    background-color: white !important;
    /* 设置背景色 */
    color: black !important;
    /* 设置字体颜色 */
    opacity: 1 !important;
    /* 取消透明效果 */
    border: 1px solid #ccc !important;
    /* 设置边框样式 */
    cursor: not-allowed;
    /* 保持不可点击的鼠标样式 */
}
</style>
