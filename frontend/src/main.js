import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import {OktaAuth} from '@okta/okta-auth-js'
import config from '@/config'
import OktaVue from '@okta/okta-vue'

const oktaAuth = new OktaAuth(config.oidc)

createApp(App)
    .use(router)
    .use(OktaVue, {oktaAuth})
    .mount('#app')
