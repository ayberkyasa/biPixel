<template>
  <v-container class="pa-16">
    <v-row class="display-1 font-weight-medium mb-6">Add a New Friend</v-row>
    <v-row
      ><v-text-field
        v-model="search"
        label="Search by name"
        filled
        rounded
        dense
      ></v-text-field
    ></v-row>
    <v-data-table
      :headers="headers"
      :items="nonFriends"
      item-key="email"
      class="elevation-1"
      :search="search"
    >
      <template v-slot:[`item.request`]="{ item }">
        <v-btn color="green lighten-1" rounded small dark @click="follow(item)"
          ><v-icon dark>mdi-account-plus</v-icon></v-btn
        >
      </template>
    </v-data-table>
    <v-divider></v-divider>
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
  },
  methods: {},
};
</script>
