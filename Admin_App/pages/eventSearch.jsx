import React from "react";
// eslint-disable-next-line no-unused-vars
import fetch from "isomorphic-unfetch";
import { Grid } from "@material-ui/core";
import Link from "next/link";
import ToolBar from "../components/ToolBar";
import auth from "../Auth";
import YouMustLogIn from "../components/YouMustLogIn";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import EventIcon from "@material-ui/icons/Event";
import useListStyle from "../styles/eventSearchStyle";
import useIndexStyle from "../styles/IndexStyle";
import Paper from "@material-ui/core/Paper";
import TextField from "@material-ui/core/TextField";

const baseURL = "http://3.133.81.46:80";

class eventSearch extends React.Component {
  constructor(props) {
    super(props);
    auth.initialise(props);
    fetch(baseURL + "/api/v1/events", {
      method: "GET",
      headers: {
        Authorization: "Token " + auth.getIdToken()
      }
    })
      .then(res => res.json())
      .then(json => this.setState({ allEvents: json, events: json }));
  }

  state = {
    allEvents: [],
    events: []
  };

  render() {
    const { events } = this.state;
    const { allEvents } = this.state;
    console.log(allEvents)
    return !auth.isAuthenticated() ? (
      <YouMustLogIn />
    ) : (
      <Grid container justify="center" style={{ marginTop: "5rem" }}>
        <Grid item xs={11} lg={8}>
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
                          return myevent.name.startsWith(event.target.value);
                        })
                      });
                    }}
                  />
                </Grid>
              }
            >
              {events.map(event => (
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
              ))}
            </List>
          </Paper>
        </Grid>
      </Grid>
    );
  }
}

export default eventSearch;
