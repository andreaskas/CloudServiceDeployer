Cloud Service Deployer
======================
Cloud Service Deployer is a standalone [TOSCA](https://www.oasis-open.org/committees/tc_home.php?wg_abbrev=tosca) service description deployer with a Cloud Connector to Openstack Private Cloud deployments. 

Cloud Connector
---------------
The Cloud Connector features other capabilities as well, such as:
- Creating Images and Snapshots
- Listing resources (Flavors, Networks, Keys, Security Groups, Instances, etc.)
- Creating and Terminating Deployments
- Returning a detailed JSON description of a successful deployment or errors that might have occured

Support for other Cloud platforms can be provided by simply implementing the ICloudConnector Interface. No other changes are required.

Cloud Application Management Framework
---------------------------------------
A more concrete implementation of the Cloud Deployer and Connectors developed can be found in the Eclipse Foundation [Cloud Application Management Framework](https://projects.eclipse.org/projects/technology.camf) or simply [CAMF] (https://projects.eclipse.org/projects/technology.camf)

Contributors
------------
- [Andreas Kastanas] (https://github.com/andreaskas)
- [Demetris Trihinas] (https://github.com/dtrihinas)

Licence
---------------
The complete source code is open-source and available to the community under the [Apache 2.0 licence](http://www.apache.org/licenses/LICENSE-2.0.html)
