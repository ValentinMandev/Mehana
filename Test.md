Here is the complete, professional documentation for your SAP BW/4HANA preparation task, structured in English based on your data model and tables.
Automotive Mini-World Documentation: Production & BOM Analysis
1. Business Idea & Scenario
This project establishes a mini-world for an automotive manufacturing environment, designed to be deployed and analyzed within SAP BW/4HANA. The focus is on Production Planning, Bill of Materials (BOM) structures, and Supply Chain Procurement. The model links vehicle models and their planned production batches with external suppliers and specific component costs, enabling multi-dimensional business analytics.
2. Data Model Overview (ER Diagram)
The data model consists of three core tables forming a relational star/snowflake schema:
 * ZVM_VEHICLES (Master Data): Contains vehicle specifications and planned production volumes.
 * ZVM_SUPPLIERS (Master Data): Contains supplier details, categories, and performance ratings.
 * ZVM_BOM_COMP (Transactional Data): Acts as the central fact/BOM structure table linking vehicle models to their required parts and respective suppliers.
 * Relationships:
   * ZVM_VEHICLES has a 1-to-Many (1:M) relationship with ZVM_BOM_COMP via VEHICLE_ID.
   * ZVM_SUPPLIERS has a 1-to-Many (1:M) relationship with ZVM_BOM_COMP via SUPPLIER_ID.
3. Table Structure & Data Dictionary
Table 1: ZVM_VEHICLES (Master Data)
Description: Stores master data for vehicle models, assembly plant locations, target costs, and planned production batch sizes.
| Column Name | Data Type | Length | Description |
|---|---|---|---|
| VEHICLE_ID (PK) | CHAR | 8 | Vehicle ID |
| MODEL_NAME | CHAR | 20 | Model Name |
| VEHICLE_TYPE | CHAR | 10 | Vehicle Type (e.g., Electric, Diesel, Hybrid) |
| ASSEMBLY_PLANT | CHAR | 30 | Assembly Plant Location |
| TARGET_PROD_COST | CURR | 7,2 | Target Production Cost |
| CURRENCY | CUKY | 5 | Currency (EUR) |
| PLANNED_PROD_BATCH | INT4 | 10 | Planned Production Batch (Units) |
Table 2: ZVM_SUPPLIERS (Master Data)
Description: Stores information regarding external component suppliers, their countries, supplied categories, and performance ratings.
| Column Name | Data Type | Length | Description |
|---|---|---|---|
| SUPPLIER_ID (PK) | CHAR | 8 | Supplier ID |
| SUPPLIER_NAME | CHAR | 30 | Supplier Name |
| COUNTRY | CHAR | 20 | Country of Origin |
| COMPONENT_CATEGORY | CHAR | 30 | Component Category Supplied |
| RATING | DEC | 1,1 | Supplier Rating (1-5) |
Table 3: ZVM_BOM_COMP (Transactional Data)
Description: Acts as the Bill of Materials transaction table, defining which parts and quantities are required per vehicle model and who supplies them.
| Column Name | Data Type | Length | Description |
|---|---|---|---|
| BOM_ID (PK) | CHAR | 8 | BOM ID |
| VEHICLE_ID (FK) | CHAR | 8 | Vehicle ID (Foreign Key) |
| PART_NAME | CHAR | 30 | Part Name |
| CATEGORY | CHAR | 20 | Part Category |
| SUPPLIER_ID (FK) | CHAR | 8 | Supplier ID (Foreign Key) |
| QUANTITY_NEEDED | INT4 | 10 | Quantity Needed per Vehicle |
| UNIT_COST | CURR | 7,2 | Unit Cost per Part |
| TOTAL_PART_COST | CURR | 7,2 | Total Part Cost per Vehicle |
| CURRENCY | CUKY | 5 | Currency (EUR) |
4. Analytical Questions to Answer in SAP BW/4HANA
 * „Which supplier (Name and Country) provides the single most expensive component based on the BOM table?“
 * „What is the total procurement cost (Total Part Cost multiplied by the Planned Production Batch) for components supplied by top-tier vendors with a rating \ge 4.8 (VoltTech AG, TurboSpeed Inc, and BrakeMaster AG)?“
 * „Which vehicle models are associated with the supplier MotoDrive GmbH, what is their planned production batch, and how do engine costs scale across those batches?“
