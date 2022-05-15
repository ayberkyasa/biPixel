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
      <v-col cols="1" sm="1" class="pl-5"> Select Category: </v-col>
      <v-col cols="5" class="mx-5">
        <v-select
          v-model="selected"
          :items="selectElements"
          attach
          label="Categories"
          item-text="value"
          return-object
        ></v-select>
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
          @click="openDetailsDialog(item)"
          ><v-icon>mdi-information</v-icon></v-btn
        >
      </template>
      <template v-slot:[`item.addFav`]="{ item }">
        <v-btn icon color="pink" v-if="checkFav(item)">
          <v-icon>mdi-heart</v-icon>
        </v-btn>
        <v-btn
          icon
          color="blue-grey lighten-3"
          v-if="!checkFav(item)"
          @click="addFavorites(item)"
        >
          <v-icon>mdi-heart</v-icon>
        </v-btn>
      </template>
      <template v-slot:[`item.rent`]="{ item }">
        <v-btn
          color="green lighten-1"
          small
          dark
          @click="showedMovie = item"
          @click.stop="rent(item)"
          >Rent</v-btn
        >
      </template>
    </v-data-table>
    <v-divider></v-divider>
    <v-dialog v-model="rentDialog" max-width="500">
      <v-card>
        <v-card-title class="text-h5 mb-3"> Payment</v-card-title>
        <v-card-text class="pl-9">
          <v-row class="text-subtitle-1">
            <strong>Card Holder's Name</strong>
          </v-row>
          <v-row>
            <v-text-field
              v-model="holderName"
              label="Name"
              filled
              rounded
              dense
            >
            </v-text-field>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Card Number</strong>
          </v-row>
          <v-row>
            <v-text-field
              v-model="cardNumber"
              label="Card Number"
              filled
              rounded
              dense
            >
            </v-text-field>
          </v-row>
          <v-row class="text-subtitle-1">
            <v-col class="mr-3" cols="7">
              <v-row>
                <strong>Expiration Date</strong>
              </v-row>
              <v-row>
                <v-text-field
                  v-model="exDate"
                  label="Expiration Date (MM/YY)"
                  filled
                  rounded
                  dense
                >
                </v-text-field>
              </v-row>
            </v-col>
            <v-col cols="4">
              <v-row>
                <strong>CVV</strong>
              </v-row>
              <v-row>
                <v-text-field v-model="cvv" label="CVV" filled rounded dense>
                </v-text-field>
              </v-row>
            </v-col>
            <v-row v-if="error">
              <span class="red--text text--darken-1">{{ errorMes }}</span>
            </v-row>
          </v-row>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn dark color="green darken-1" @click="pay">
            PAY ({{ this.rentPrice }} TL)
          </v-btn>
          <v-btn dark color="red darken-1" @click="rentDialog = false">
            CLOSE
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="detailsDialog" max-width="500">
      <v-card>
        <v-card-title class="text-h5 mb-3"> Movie Details</v-card-title>
        <v-card-text class="pl-9">
          <v-row class="text-subtitle-1">
            <strong>Duration: {{ showedMovie.duration }} (min)</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Language Option: {{ showedMovie.language_option }}</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Subtitle Option: {{ showedMovie.subtitle_option }}</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Actors: {{ actors }}</strong>
          </v-row>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn dark color="red darken-1" @click="detailsDialog = false">
            CLOSE
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-snackbar color="green lighten-1" timeout="2000" v-model="snackbar">
      Movie was rented
    </v-snackbar>
  </v-container>
</template>
<script>
import axiosInstance, { URL } from "../services/axiosConfig";
export default {
  data() {
    return {
      errorMes: "",
      snackbar: false,
      error: false,
      holderName: "",
      cardNumber: "",
      exDate: "",
      cvv: "",
      favorites: [],
      actors: "",
      showedMovie: {},
      detailsDialog: false,
      rentDialog: false,
      search: "",
      price: "",
      rentPrice: "",
      rate: "",
      selected: "",
      movies: [],
      selectElements: [],
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
          value: "directors",
          sortable: false,
        },
        {
          text: "Genre",
          sortable: false,
          align: "start",
          value: "genres",
          filter: (value) => {
            if (this.selected === "") {
              return true;
            } else {
              var check = false;
              value.forEach(item => {
                if(this.selected === item){
                  check = true;
                }
              })
              return check;
            }
          },
        },
        {
          text: "Rate",
          align: "start",
          value: "overall_rating",
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
          value: "production_year",
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
        {
          text: "",
          align: "center",
          value: "rent",
          filterable: false,
          sortable: false,
        },
      ];
    },
  },
  methods: {
    async rent(item) {
      if (this.$store.state.userType !== "Employee") {
        console.log("girdi");
        this.openRentDialog(item);
      } else {
        try {
          const res = await axiosInstance.post(URL.RENT, {
            movieId: this.showedMovie.mid,
            userId: this.$store.state.uid,
          });
          console.log(res);
          try {
            const res = await axiosInstance.get(URL.SEARCH_MOVIE, {
              params: {
                key: this.search,
                userId: this.$store.state.uid,
              },
            });
            await this.getFavorites();
            this.movies = res.data;
          } catch (error) {
            console.log(error.response);
          }
        } catch (error) {
          console.log(error);
        }
      }
    },
    checkFav(item) {
      var check = false;
      this.favorites.forEach((elm) => {
        if (elm.movie_id === item.mid) {
          check = true;
        }
      });
      return check;
    },
    async addFavorites(value) {
      try {
        const res = await axiosInstance.post(URL.ADD_FAVORITE, {
          movieId: value.mid,
          userId: this.$store.state.uid,
        });
        console.log(res);
        await this.getFavorites();
      } catch (error) {
        console.log(error);
      }
    },
    showActors() {
      this.showedMovie.actors.forEach((item) => {
        this.actors += item + ", ";
      });
    },
    openDetailsDialog(item) {
      this.showedMovie = item;
      this.detailsDialog = true;
      this.showActors();
    },
    openRentDialog(item) {
      this.showedMovie = item;
      if (this.$store.state.userType == "Employee") {
        this.rentPrice = 0;
      } else {
        this.rentPrice = item.price;
      }
      this.rentDialog = true;
    },
    async getFavorites() {
      try {
        const res = await axiosInstance.get(URL.GET_FAVORITE_LIST, {
          params: {
            userId: this.$store.state.uid,
          },
        });
        this.favorites = res.data;
      } catch (error) {
        console.log("err");
      }
    },
    async pay() {
      var checkEx = this.checkExDate();
      var checkCVV = this.checkCVV();
      if (
        (this.holderName === "") |
        (this.cardNumber === "") |
        (this.exDate === "") |
        (this.cvv === "")
      ) {
        this.errorMes = "ALL FIELDS MUST BE FILLED";
        this.error = true;
      } else if (
        this.containsNumber(this.holderName) |
        isNaN(this.cardNumber) |
        !checkEx |
        !checkCVV |
        (this.cardNumber.length != 16)
      ) {
        this.errorMes = "FIELDS TYPES ARE NOT CORRECT";
        this.error = true;
      } else {
        this.error = false;
        this.snackbar = true;
        this.rentDialog = false;
        this.holderName = "";
        this.cardNumber = "";
        this.exDate = "";
        this.exDate = "";
        this.cvv = "";
        try {
          const res = await axiosInstance.post(URL.RENT, {
            movieId: this.showedMovie.mid,
            userId: this.$store.state.uid,
          });
          console.log(res);
          try {
            const res = await axiosInstance.get(URL.SEARCH_MOVIE, {
              params: {
                key: this.search,
                userId: this.$store.state.uid,
              },
            });
            await this.getFavorites();
            this.movies = res.data;
          } catch (error) {
            console.log(error.response);
          }
        } catch (error) {
          console.log(error);
        }
      }
    },
    checkExDate() {
      if (this.exDate.length != 5) {
        return false;
      } else if (
        isNaN(this.exDate[0]) |
        isNaN(this.exDate[1]) |
        (this.exDate[2] != "/") |
        isNaN(this.exDate[3]) |
        isNaN(this.exDate[4])
      ) {
        return false;
      } else {
        return true;
      }
    },
    checkCVV() {
      if ((this.cvv.length != 3) | isNaN(this.cvv)) {
        return false;
      } else {
        return true;
      }
    },
    containsNumber(str) {
      return /\d/.test(str);
    },
  },
  async created() {
    try {
      const res = await axiosInstance.get(URL.SEARCH_MOVIE, {
        params: {
          key: this.search,
          userId: this.$store.state.uid,
        },
      });

      const genres = await axiosInstance.get(URL.GET_GENRES);
      this.selectElements = genres.data;
      console.log(this.selectElements);
      await this.getFavorites();
      this.movies = res.data;
    } catch (error) {
      console.log(error.response);
    }
  },
};
</script>
