package org.foo;

class Utilities {

	def steps
  	Utilities(steps) {this.steps = steps}
  	def mvn(args) {
  		steps.echo "using steps.echo"
    	steps.bat "${steps.tools 'Maven311'}/bin/mvn --version"    	
  	}
}


