<template>
  <v-container class="pa-16">
    <v-row class="display-1 font-weight-medium mb-6">Social Network</v-row>
    <v-row
      ><v-text-field
        v-model="search"
        label="Search by name or email if you know"
        filled
        rounded
        dense
      ></v-text-field
    ></v-row>
    <v-tabs color="deep-blue accent-4" v-model="tab">
      <v-tabs-slider color="blue"></v-tabs-slider>
      <v-tab>Add Friend</v-tab>
      <v-tab>Remove Friend</v-tab>
      <v-tab>Sent and Pending Requests</v-tab>
      <v-tab>Received and Pending Requests</v-tab>
    </v-tabs>
    <v-tabs-items v-model="tab">
      <v-tab-item>
        <v-data-table
          :headers="headers"
          :items="nonFriends"
          item-key="email"
          class="elevation-1"
          :search="search"
        >
          <template v-slot:[`item.request`]="{ item }">
            <v-btn
              color="green darken-1"
              rounded
              small
              dark
              @click="addFriend(item)"
              ><v-icon dark>mdi-account-plus</v-icon></v-btn
            >
          </template>
        </v-data-table>
      </v-tab-item>
      <v-tab-item>
        <v-data-table
          :headers="headers"
          :items="friends"
          item-key="email"
          class="elevation-1"
          :search="search"
        >
          <template v-slot:[`item.request`]="{ item }">
            <v-btn
              color="red darken-1"
              rounded
              small
              dark
              @click="removeFriend(item)"
              ><v-icon dark>mdi-account-remove</v-icon></v-btn
            >
          </template>
        </v-data-table>
      </v-tab-item>
      <v-tab-item>
        <v-data-table
          :headers="headers2"
          :items="sentPending"
          item-key="email"
          class="elevation-1"
          :search="search"
        >
        </v-data-table>
      </v-tab-item>
      <v-tab-item>
        <v-data-table
          :headers="headers3"
          :items="receivedPending"
          item-key="email"
          class="elevation-1"
          :search="search"
        >
          <template v-slot:[`item.accept`]="{ item }">
            <v-btn
              color="green darken-1"
              rounded
              small
              dark
              @click="acceptRequest(item)"
              ><v-icon dark>mdi-checkbox-marked-circle</v-icon></v-btn
            >
          </template>
          <template v-slot:[`item.reject`]="{ item }">
            <v-btn
              color="red darken-1"
              rounded
              small
              dark
              @click="rejectRequest(item)"
              ><v-icon dark>mdi-close-circle</v-icon></v-btn
            >
          </template>
        </v-data-table>
      </v-tab-item>
    </v-tabs-items>
  </v-container>
</template>
<script>
import axiosInstance, { URL } from "../services/axiosConfig";
export default {
  data() {
    return {
      search: "",
      nonFriends: [],
      friends: [],
      sentPending: [],
      receivedPending: [],
      tab: null,
    };
  },
  computed: {
    headers() {
      return [
        {
          text: "Full Name",
          align: "start",
          value: "full_name",
          sortable: false,
        },
        {
          text: "Email",
          align: " d-none",
          value: "email",
          sortable: false,
        },
        {
          text: "Send Request",
          sortable: false,
          align: "center",
          value: "request",
        },
      ];
    },
    headers2() {
      return [
        {
          text: "Full Name",
          align: "start",
          value: "full_name",
          sortable: false,
        },
        {
          text: "Email",
          align: " d-none",
          value: "email",
          sortable: false,
        },
      ];
    },
    headers3() {
      return [
        {
          text: "Full Name",
          align: "start",
          value: "full_name",
          sortable: false,
        },
        {
          text: "Email",
          align: " d-none",
          value: "email",
          sortable: false,
        },
        {
          text: "Accept",
          sortable: false,
          align: "center",
          value: "accept",
        },
        {
          text: "Reject",
          sortable: false,
          align: "center",
          value: "reject",
        },
      ];
    },
  },
  methods: {
    // async addFriend(item) {
    //   try {
    //       const res = awia
    //   } catch (error) {

    //   }
    // },
    async initialize() {
      try {
        const nonFriends = await axiosInstance.get(URL.GET_NON_FRIENDS, {
          params: {
            userId: this.$store.state.uid,
          },
        });
        this.nonFriends = nonFriends.data;
      } catch (error) {
        console.log(error);
      }

      try {
        const friends = await axiosInstance.get(URL.GET_FRIENDS, {
          params: {
            userId: this.$store.state.uid,
          },
        });
        this.friends = friends.data;
      } catch (error) {
        console.log(error);
      }

      try {
        const receiving = await axiosInstance.get(
          URL.GET_RECEIVING_FRIEND_REQUESTS,
          {
            params: {
              userId: this.$store.state.uid,
            },
          }
        );
        this.receivedPending = receiving.data;
      } catch (error) {
        console.log(error);
      }

      try {
        const sent = await axiosInstance.get(URL.GET_SENT_FRIEND_REQUESTS, {
          params: {
            userId: this.$store.state.uid,
          },
        });
        this.sentPending = sent.data;
      } catch (error) {
        console.log(error);
      }
    },
    async addFriend(item) {
      try {
        const res = await axiosInstance.post(URL.ADD_FRIEND, {
          whoSendRequest: this.$store.state.uid,
          whoReceiveRequest: item.user_id,
        });
        await this.initialize();
        console.log(res);
      } catch (error) {
        console.log(error);
      }
    },
    async removeFriend(item) {
      try {
        const res = await axiosInstance.delete(URL.DELETE_FRIEND, {
          data: {
            whoDelete: this.$store.state.uid,
            whoIsDeleted: item.user_id,
          },
        });
        console.log(res);
        await this.initialize();
      } catch (error) {
        console.log(error);
      }
      return item;
    },
    async acceptRequest(item) {
      try {
        const res = await axiosInstance.post(URL.ANSWER_REQUESTS, {
          requestId: "true",
          whoSendRequest: item.user_id,
          whoAnswerRequest: this.$store.state.uid,
        });
        console.log(res);
        await this.initialize();
      } catch (error) {
        console.log(error);
      }
    },
    async rejectRequest(item) {
      try {
        const res = await axiosInstance.post(URL.ANSWER_REQUESTS, {
          requestId: "false",
          whoSendRequest: item.user_id,
          whoAnswerRequest: this.$store.state.uid,
        });
        console.log(res);
        await this.initialize();
      } catch (error) {
        console.log(error);
      }
    },
  },
  async created() {
    await this.initialize();
  },
};
</script>
