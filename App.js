/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, { Component } from "react";
import {
  StyleSheet,
  Text,
  View,
  TextInput,
  Button,
  TouchableOpacity
} from "react-native";
import ToastExample from "./JsBridge";
type Props = {};
export default class App extends Component<Props> {
  constructor(props) {
    super(props);
    this.state = { inputString: "" };
  }

  sendToNative = () => {
    ToastExample.showNotification(this.state.inputString);
  };
  onSearchTextChanged = event => {
    this.setState({ inputString: event.nativeEvent.text });
  };
  render() {
    return (
      <View style={styles.container}>
        <View style={styles.container}>
          <TextInput
            underlineColorAndroid={"transparent"}
            style={styles.textInput}
            value={this.state.inputString}
            onChange={this.onSearchTextChanged}
            placeholder="Entered text will be pass to android native"
          />

          <TouchableOpacity style={styles.button} onPress={this.sendToNative}>
            <Text> Send </Text>
          </TouchableOpacity>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    padding: 10,
    marginTop: 20,
    alignItems: "center"
  },
  textInput: {
    height: 36,
    padding: 5,
    marginRight: 5,
    fontSize: 15,
    borderWidth: 1,
    borderColor: "black",
    borderRadius: 3,
    color: "black"
  },
  button: {
    alignItems: "center",
    marginTop: 30,
    paddingLeft: 50,
    paddingRight: 50,
    borderRadius: 5,
    backgroundColor: "#DDDDDD",
    padding: 10
  }
});
