/**
 *  WorkingWithTime
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
    name: "WorkingWithTime",
    namespace: "saipavandarsi",
    author: "saipavan",
    description: "Triggering events in home by monitoring time",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Select SmartThings") {
    	input "openCloseSensor","capability.contactSensor",title:"which door?", required:true, multiple:false
        input "roomLight","capability.switch",title:"which room light?", required:true,multiple:false
	}
    section("Turn on between what times?") {
    	input "fromTime","time",title:"From", required:true
        input "toTime","time",title:"To", required:true
	}
    section("on which Days") {
    	input "days","enum",title:"select Days of the Week", required:true,options: ["Monday": "Monday", "Tuesday": "Tuesday", "Wednesday": "Wednesday", "Thursday": "Thursday", "Friday": "Friday"]
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
	subscribe(openCloseSensor,"contact.open",contactHandler)
}

def contactHandler(evt){
	log.debug "Door is opened. Now check if the current time is within the visiting hours window"
     def df = new java.text.SimpleDateFormat("EEEE")
     df.setTimeZone(location.timeZone)
     Date d = new Date()
     log.debug "date is ::::$d"
     def day = df.format(d)
    def dayCheck = days.contains(day)
    if (dayCheck) {
        def between = timeOfDayIsBetween(fromTime, toTime, new Date(), location.timeZone)
        if (between) {
            roomLight.on()
        } else {
            roomLight.off()
        }
    }
}

