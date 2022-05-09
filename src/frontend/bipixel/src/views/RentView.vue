<template>
  <v-container class="pa-16">
    <v-row class="display-1 font-weight-medium mb-6">Rent a Movie</v-row>
    <v-row
      ><v-text-field
        v-model="search"
        label="Search by name"
        filled
        rounded
        dense
      ></v-text-field
    ></v-row>
    <v-row class="align-center mb-2">
      <v-col cols="12" sm="1" class="pl-5"> Select Category: </v-col>
      <v-col cols="12" sm="11">
        <v-chip-group mandatory active-class="primary--text">
          <v-chip
            v-for="el in selectElements"
            :key="el.key"
            @click="selected = el.value"
            >{{ el.value }}</v-chip
          >
        </v-chip-group>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2"> Select Upper Threshold of Price: </v-col>
      <v-col cols="4">
        <v-text-field
          class="mr-10"
          v-model="price"
          label="Enter a price"
          filled
          rounded
          dense
        ></v-text-field>
      </v-col>
      <v-col cols="2"> Select Lower Threshold of Rate: </v-col>
      <v-col cols="4">
        <v-text-field
          class="mr-10"
          v-model="rate"
          label="Enter a rate between 1 and 10"
          filled
          rounded
          dense
        ></v-text-field>
      </v-col>
    </v-row>
    <v-data-table
      :headers="headers"
      :items="movies"
      item-key="email"
      class="elevation-1"
      :search="search"
    >
      <template v-slot:[`item.details`]="{ item }">
        <v-btn
          color="green lighten-1"
          icon
          large
          @click="showedMovie = item"
          @click.stop="detailsDialog = true"
          ><v-icon>mdi-information</v-icon></v-btn
        >
      </template>
      <template v-slot:[`item.addFav`]="{ item }">
        <v-btn icon color="pink" @click="addFavorites(item)">
          <v-icon>mdi-heart</v-icon>
        </v-btn>
        <v-btn icon color="blue-grey lighten-3" @click="addFavorites(item)">
          <v-icon>mdi-heart</v-icon>
        </v-btn>
      </template>
    </v-data-table>
    <v-divider></v-divider>
    <v-dialog v-model="detailsDialog" max-width="500">
      <v-card>
        <v-card-title class="text-h5 mb-3"> Movie Details </v-card-title>

        <v-card-text class="pl-9">
          <v-row class="text-subtitle-1">
            <strong>Duration: </strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Language Option: </strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Subtitle Option: </strong>
          </v-row>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="detailsDialog = false">
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>
<script>
export default {
  data() {
    return {
      showedMovie: {},
      detailsDialog: false,
      search: "",
      price: "",
      rate: "",
      selected: "None",
      movies: [
        {
          title: "Movie 1",
          director: "Director 1",
          genre: "Action",
          rate: "8",
          year: "2019",
          price: "10",
        },
        {
          title: "Movie 2",
          director: "Director 1",
          genre: "Comedy",
          rate: "8.5",
          year: "2018",
          price: "20",
        },
        {
          title: "Movie 3",
          director: "Director 2",
          genre: "Science Fiction",
          rate: "7",
          year: "2019",
          price: "150",
        },
        {
          title: "Movie 4",
          director: "Director 3",
          genre: "Mystery",
          rate: "8",
          year: "2019",
          price: "40",
        },
        {
          title: "Movie 5",
          director: "Director 2",
          genre: "Action",
          rate: "8",
          year: "2021",
          price: "5",
        },
        {
          title: "Movie 6",
          director: "Director 4",
          genre: "Science Fiction",
          rate: "8",
          year: "2019",
          price: "80",
        },
        {
          title: "Movie 7",
          director: "Director 3",
          genre: "Mystery",
          rate: "8",
          year: "2019",
          price: "65",
        },
        {
          title: "Movie 8",
          director: "Director 4",
          genre: "Comedy",
          rate: "8",
          year: "2019",
          price: "110",
        },
      ],
      selectElements: [
        {
          key: "none",
          value: "None",
        },
        {
          key: "action",
          value: "Action",
        },
        {
          key: "comedy",
          value: "Comedy",
        },
        {
          key: "sci_fi",
          value: "Science Fiction",
        },
        {
          key: "romance",
          value: "Romance",
        },
        {
          key: "mystery",
          value: "Mystery",
        },
      ],
    };
  },
  computed: {
    headers() {
      return [
        {
          text: "Title",
          align: "start",
          value: "title",
          sortable: false,
        },
        {
          text: "Director",
          align: "start",
          value: "director",
          filterable: false,
          sortable: false,
        },
        {
          text: "Genre",
          sortable: false,
          align: "start",
          value: "genre",
          filter: (value) => {
            if (this.selected === "None") {
              return true;
            } else {
              return value === this.selected;
            }
          },
        },
        {
          text: "Rate",
          align: "start",
          value: "rate",
          sortable: false,
          filter: (value) => {
            if (this.rate === "") {
              return true;
            } else {
              return value >= parseInt(this.rate);
            }
          },
        },
        {
          text: "Year",
          align: "start",
          filterable: false,
          value: "year",
          sortable: false,
        },
        {
          text: "Price (TL)",
          align: "start",
          value: "price",
          sortable: false,
          filter: (value) => {
            if (this.price === "") {
              return true;
            } else {
              return value <= parseInt(this.price);
            }
          },
        },
        {
          text: "Details",
          align: "center",
          value: "details",
          filterable: false,
          sortable: false,
        },
        {
          text: "Add Favorites",
          align: "center",
          value: "addFav",
          filterable: false,
          sortable: false,
        },
      ];
    },
  },
  methods: {
    addFavorites(value) {
      console.log(value, "fav");
    },
  },
};
</script>
