<template>
    <view class="content">
        <view>{{ token }}</view>
        <navigator url="/pages/user/login" open-type="navigate" hover-class="navigator-hover">
            跳转
        </navigator>
    </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'


const token = ref('')
uni.getStorage({
    key: 'Authorization'
}).then((res: any) => {
    token.value = res.data
})
</script>

<style lang="scss">
.content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    view {
        max-width: 600px;
        margin-left: auto;
        margin-right: auto;
        border: 2px solid red;
        overflow-wrap: break-word;
        
    }
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
</style>
