************************************************************************
* Getting Started with SAP HANA Cloud Platform                         *
* Source Code Bundle                                                   *
* ==================================================================== *
This directory contains all of the source code and test data artifacts
needed to re-create the bookstore example demonstrated in Chapter 5. To
deploy this example, you'll need to do a find-and-replace on the string
token {Your HCP Trial Account} in three files:

bookstore/storefront/model/BookService.xsodata
bookstore/storefront/model/Bookstore.hdbdd
bookstore/storefront/ui/booklist.xsjs

Note that you may also need to change the schema/package references 
here if you decided not to use the names given in the example.

The CSV sample files are contained within the test_data folder.