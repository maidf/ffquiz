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
                <uni-easyinput disabled type="text" v-model="user.create_time" />
            </uni-forms-item>
        </uni-forms>

        <button size="mini" @click="update_usr_msg(token)">
            更新信息
        </button>
        <button size="mini" @click="to_upd_password">
            修改密码
        </button>
        <button size="mini" @click="delete_usr">
            注销账号
        </button>
    </view>
</template>

<script setup lang="ts">
import { useUsrStore } from '@/stores/usr'
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
    create_time: Date | null
}

const user = ref<usr>({
    id: null,
    account: null,
    name: null,
    email: null,
    role: null,
    create_time: null
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
            uni.showToast({ title: res.data.toString(), icon: 'none' })
            fetch_usr_msg(token)
        }
    }).catch(err => uni.showToast({ title: err, icon: 'none' }))
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
            uni.showToast({ title: res.data.toString(), icon: 'none' })
        }
    }).catch(err => uni.showToast({ title: err, icon: 'none' }))
}


const to_upd_password = () => {
    uni.navigateTo({ url: "/pages/user/upd-pass" })
}

const delete_usr = () => {
    uni.navigateTo({ url: "/pages/user/logoff" })
    const send = useUsrStore().logoff
    send()
}

</script>

<style lang="scss">
.content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

}

button {
    margin-bottom: 10px;
}
</style>
