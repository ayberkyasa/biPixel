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
              color="green lighten-1"
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
              color="red lighten-1"
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
              color="green lighten-1"
              rounded
              small
              dark
              @click="acceptRequest(item)"
              ><v-icon dark>mdi-checkbox-marked-circle</v-icon></v-btn
            >
          </template>
          <template v-slot:[`item.reject`]="{ item }">
            <v-btn
              color="red lighten-1"
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
export default {
  data() {
    return {
      search: "",
      nonFriends: [
        { full_name: "ayberk", email: "ayberkyasa@gmail.com" },
        { full_name: "fatih", email: "fatih@gmail.com" },
        { full_name: "doğa", email: "doga@gmail.com" },
        { full_name: "esra", email: "esra@gmail.com" },
      ],
      friends: [
        { full_name: "a", email: "ayberkyasa@gmail.com" },
        { full_name: "f", email: "fatih@gmail.com" },
        { full_name: "d", email: "doga@gmail.com" },
        { full_name: "e", email: "esra@gmail.com" },
      ],
      sentPending: [
        { full_name: "a", email: "ayberkyasa@gmail.com" },
        { full_name: "f", email: "fatih@gmail.com" },
      ],
      receivedPending: [
        { full_name: "a", email: "ayberkyasa@gmail.com" },
        { full_name: "f", email: "fatih@gmail.com" },
      ],
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
    async addFriend(item) {
      return item;
    },
    async removeFriend(item) {
      return item;
    },
    async acceptRequest(item) {
      return item;
    },
    async rejectRequest(item) {
      return item;
    },
  },
};
</script>
