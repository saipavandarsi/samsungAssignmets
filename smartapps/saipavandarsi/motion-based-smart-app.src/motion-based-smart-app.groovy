/**
 *  Motion Based Smart App
 *
 *  Copyright 2017 saipavan
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Motion Based Smart App",
    namespace: "saipavandarsi",
    author: "saipavan",
    description: "This app is used to change the switch status depending on motion with in certain time frame.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Turn on when motion detected:") {
    	input "themotion","capability.motionSensor",required: true,title: "where?"
	}
    section("Turn off when there is no movement for") {
    	input "minutes","number",required: true,title: "Minutes?"
	}
    section("Turn on this light") {
    	input "theswitch","capability.switch",required: true
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(themotion,"motion.active",motionDetectedHandler)
    subscribe(themotion,"motion.inactive",motionStoppedHandler)
}

def motionDetectedHandler(evt){
	log.debug "motionDetectedHandler event called"
    theswitch.on()
}
def motionStoppedHandler(evt){
	log.debug "motionStoppedHandler event called"
    runIn(minutes, checkMotion)
}
def checkMotion(){
	log.debug "In checkMotion scheduled method"
    def motionState = themotion.currentState("motion")
    if(motionState.value == "inactive"){
    	def elapsed = now() - motionState.date.time
        log.debug "elapsed time in ms:$elapsed"
        def threshold =  1000 *2* minutes
        log.debug "treshold time in ms:$threshold"
        if( elapsed >= threshold){
        	log.debug "Motion has stayed inactive long enough since last check ($elapsed ms): turning switch off"
            theswitch.off()
        }else{
        	log.debug "Motion has not stayed inactive long enough since last check ($elapsed ms): doing noting"
        }
    }
     else{
     	log.debug "Motion is active, doing nothing and waiting for inactive"
     }
}

