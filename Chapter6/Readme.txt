************************************************************************
* Getting Started with SAP HANA Cloud Platform                         *
* Source Code Bundle                                                   *
* ==================================================================== *
Note: When deploying Example 6.1, bear in mind that the initial
      deployment will fail due to resource reference errors. This is
      a function of missing destination definitions which need to be
      created in the HCP Cockpit. Here, the steps are as follows:
        1) From the landing page, click on the Java Applications
           tab.
        2) Click on the (failed) application link.
        3) Then, in the Java Application Dashboard, click on the
           Destinations sub-tab.
        4) Fill in the destination files included in this directory
           and upload them into the console.
        5) Finally, re-start the application and it should bind to
           the resources correctly.

Also, note that if you want to test the REST call to the Google
Geocode API, you'll need to plug in your API key in the web.xml
deployment descriptor file.