import React from "react";
import fetch from "isomorphic-unfetch";
import { Grid } from "@material-ui/core";
import TextField from "@material-ui/core/TextField";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import EventIcon from "@material-ui/icons/Event";
import Paper from "@material-ui/core/Paper";
import Link from "next/link";
import ToolBar from "../components/ToolBar";
import auth from "../Auth";
import YouMustLogIn from "../components/YouMustLogIn";
import useListStyle from "../styles/eventSearchStyle";
import useIndexStyle from "../styles/IndexStyle";
import FormDialogFab from "../components/FormDialogFabEvent";
import CircularIndeterminate from "../components/CircularIndeterminate";

class eventSearch extends React.Component {
  constructor(props) {
    super(props);
    auth.initialise(props);
  }

  state = {
    allEvents: [],
    events: null
  };

  componentDidMount() {
    fetch(process.env.SERVER_BASE_URL + "/api/v1/events", {
      method: "GET",
      headers: {
        Authorization: "Token " + auth.getIdToken()
      }
    })
      .then(res => res.json())
      .then(json => this.setState({ allEvents: json, events: json }));
  }

  handleNewEvent = responseJson => {
    this.setState({
      allEvents: this.state.allEvents.concat(responseJson),
      events: this.state.events.concat(responseJson)
    });
  };

  handleCreate(name, location) {
    return new Promise(async (resolve, reject) => {
      const response = await fetch(process.env.SERVER_BASE_URL + "/api/v1/event", {
        method: "POST",
        body: JSON.stringify({
          name: name,
          location: location
        }),
        headers: {
          "Content-Type": "application/json",
          Authorization: "Token " + auth.getIdToken()
        }
      });
      const responseJson = await response.json();
      if (!responseJson || !responseJson.name) {
        return reject();
      }
      resolve(responseJson);
    });
  }

  render() {
    const { events } = this.state;
    const { allEvents } = this.state;
    return !auth.isAuthenticated() ? (
      <YouMustLogIn />
    ) : (
      <div>
        <Grid container justify="center" style={{ marginTop: "5rem" }}>
          <Grid item={true} xs={11} lg={8}>
            <Paper style={useListStyle.paper}>
              <ToolBar />
              <List
                style={{ width: "100%" }}
                subheader={
                  <Grid
                    container
                    justify="center"
                    direction="row"
                    alignItems="center"
                  >
                    <TextField
                      style={{
                        marginLeft: "1%",
                        marginRight: "1%",
                        marginBottom: "1%"
                      }}
                      className={useIndexStyle.textField}
                      InputProps={{
                        className: useIndexStyle.input
                      }}
                      label="Search"
                      name="Search"
                      variant="outlined"
                      onChange={event => {
                        this.setState({
                          events: allEvents.filter(myevent => {
                            return myevent.name
                              .toLowerCase()
                              .startsWith(event.target.value.toLowerCase());
                          })
                        });
                      }}
                    />
                  </Grid>
                }
              >
                {events === null ? (
                  <CircularIndeterminate />
                ) : (
                  events.map(event => (
                    <Link
                      key={event.event_key}
                      href={{
                        pathname: "/event/[eventKey]",
                        query: { event: JSON.stringify(event) }
                      }}
                      as={`/event/${event.event_key}`}
                    >
                      <ListItem button>
                        <ListItemIcon>
                          <EventIcon />
                        </ListItemIcon>
                        <ListItemText
                          primary={event.name}
                          secondary={event.location}
                        />
                      </ListItem>
                    </Link>
                  ))
                )}
              </List>
            </Paper>
          </Grid>
        </Grid>
        <FormDialogFab
          executeCreate={this.handleCreate}
          handleNewEvent={this.handleNewEvent}
        />
      </div>
    );
  }
}

export default eventSearch;
