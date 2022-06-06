package database;

import database.settings.Settings;
import lombok.Data;
import resource.DBNode;
import resource.data.Row;
import resource.enums.AttributeType;
import resource.enums.ConstraintType;
import resource.implementation.Attribute;
import resource.implementation.AttributeConstraint;
import resource.implementation.Entity;
import resource.implementation.InformationResource;
import utils.Config;
import utils.Table;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MYSQLrepository implements Repository{

    private Settings settings;      //ip servera, username, password, ime baze
    private Connection connection;  //konekcija

    public MYSQLrepository(Settings settings) {
        this.settings = settings;
    }

    private void initConnection() throws SQLException, ClassNotFoundException{
        String ip = (String) settings.getParameter("mysql_ip");
        String database = (String) settings.getParameter("mysql_database");
        String username = (String) settings.getParameter("mysql_username");
        String password = (String) settings.getParameter("mysql_password");
        //Class.forName("net.sourceforge.jtds.jdbc.Driver");
        ////KONEKTUJEMO SE NA BAZU              (PROTOKOL:PODPROTOKOL://IPSERVERA/BAZA)
        connection = DriverManager.getConnection("jdbc:mysql://"+ip+"/"+database,username,password);


    }

    private void closeConnection(){
        try{
            connection.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            connection = null;
        }
    }


    @Override
    public DBNode getSchema() {

        try{
            this.initConnection();      //ostvarujemo konekciju

            DatabaseMetaData metaData = connection.getMetaData();       //dovlacimo metapodatke iz konekcije
            InformationResource ir = new InformationResource("RAF_BP_Primer");      //root


            String tableType[] = {"TABLE"};
            //iz metapodataka ucitavamo sve tabele koje postoje u bazi i vraca resultSet
            ResultSet tables = metaData.getTables(connection.getCatalog(), null, null, tableType);

            while (tables.next()){  //ideemo kroz resultSet

                //UZIMAMO IME TABELE
                String tableName = tables.getString("TABLE_NAME");
                //AKO JE NEKA SISTEMSKA TABELA, PRESKACEMO
                if(tableName.contains("trace"))continue;
                //PRAVIMO NOVU TABELU TJ ENTITET I PROSLEDJUJEMO ROOT KAO RODITELJA
                Entity newTable = new Entity(tableName, ir);
                Table table = new Table(tableName);
                Config.tables.add(table);
                //I UVEZUJEMO ENTITET KAO DETE ROOT-A
                ir.addChild(newTable);

                //Koje atribute imaja ova tabela?

                //UZIMAMO KOLONE KOJEE SE NALAZE U TRENUTNOJ TABELI
                ResultSet columns = metaData.getColumns(connection.getCatalog(), null, tableName, null);

                while (columns.next()){ //IDEMO KROZ KOLONE

                    // COLUMN_NAME TYPE_NAME COLUMN_SIZE ....

                    String columnName = columns.getString("COLUMN_NAME");       //UZIMAMO IME KOLONE
                    String columnType = columns.getString("TYPE_NAME");     //UZIMAMO TIP PODATKA ZA ATRIBUT

                    table.getColumns().add(columnName);

                    System.out.println(columnType);

                    int columnSize = Integer.parseInt(columns.getString("COLUMN_SIZE"));        //UZIMAMO PODATAK O VELICINI


                    //PRAVIMO ATRIBUT I PROSLEDJUJEMO PODATKE KOJE SMO UZELI IZ RS
                    //RODITELJ MU JE TRENUTNA TABELA
                    Attribute attribute = new Attribute(columnName, newTable,
                            AttributeType.valueOf(
                                    Arrays.stream(columnType.toUpperCase().split(" "))
                                    .collect(Collectors.joining("_"))),
                            columnSize);

                    ResultSet pkeys = metaData.getPrimaryKeys(connection.getCatalog(), null, tableName);

                    while (pkeys.next()){
                        String pkColumnName = pkeys.getString("COLUMN_NAME");

                        if (pkColumnName.equals(columnName)){
                            AttributeConstraint pk = new AttributeConstraint(pkColumnName, attribute, ConstraintType.PRIMARY_KEY);
                            table.getPrimaryKeys().add(pkColumnName);
                            attribute.addChild(pk);
                        }
                    }

//                    ResultSet foreignKeys = metaData.getImportedKeys(connection.getCatalog(), null, tableName);
//
//                    while (foreignKeys.next()){
//                        String fkColumnName = foreignKeys.getString("COLUMN_NAME");
//
//                        if (fkColumnName.equals(columnName)){
//                            AttributeConstraint fk = new AttributeConstraint(fkColumnName, attribute, ConstraintType.FOREIGN_KEY);
//                            attribute.addChild(fk);
//                        }
//
//
//                    }
                    newTable.addChild(attribute);       //DODAJEEMO ATRIBUT KAO DETE TRENUTNOJ TABELI

                }



            }


            //TODO Ogranicenja nad kolonama? Relacije?


            return ir;
            //String isNullable = columns.getString("IS_NULLABLE");
            // ResultSet foreignKeys = metaData.getImportedKeys(connection.getCatalog(), null, table.getName());
            // ResultSet primaryKeys = metaData.getPrimaryKeys(connection.getCatalog(), null, table.getName());

        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        catch (ClassNotFoundException e2){ e2.printStackTrace();}
        finally {
            this.closeConnection();
        }

        return null;
    }

    @Override
    public List<Row> get(String query, String from) {

        List<Row> rows = new ArrayList<>();     //PRAZNA LISTA KOJU POPUNJAVAMO


        try{
            this.initConnection();      //OSTVARILI SMO KONEKCIJU

            PreparedStatement preparedStatement = connection.prepareStatement(query);   //PRIPREMA STRING KOJI TREBA DA SE IZVRSI   => QUERY KOJI JE NA CEKANJU

            ResultSet rs = preparedStatement.executeQuery();            //executeQuery() izvrsava query i vraca ResultSet(tabelu sa podacima)
            ResultSetMetaData resultSetMetaData = rs.getMetaData();     //ResultSet sadrzi metapodatke o tabeli


            while (rs.next()){      //prolazi pokazivacem kroz tabelu

                Row row = new Row();        //pravi novi red
                row.setName(from);

                for (int i = 1; i<=resultSetMetaData.getColumnCount(); i++){
                    row.addField(resultSetMetaData.getColumnName(i), rs.getString(i));      //prolazi kroz kolone i uzima podatke i smesta u red
                }
                rows.add(row);


            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.closeConnection();
        }

        return rows;    ///VRACA LISTU REDOVA ZA PRIKAZ U TABELI
    }

    public void insertData(String file){
        int batchSize = 20;
        try {

            this.initConnection();

            connection.setAutoCommit(false);

            String sql = "INSERT INTO countries (country_id, country_name, region_id) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(file));
            String lineText = null;

            int count = 0;

           // lineReader.readLine(); // skip header line

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                String country_id = data[0];
                String country_name = data[1];
                int region_id = Integer.parseInt(data[2]);


                statement.setString(1, country_id);
                statement.setString(2, country_name);
                statement.setInt(3, region_id);


                statement.addBatch();

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }

            lineReader.close();

            // execute the remaining queries
            statement.executeBatch();

            connection.commit();
            connection.close();

        } catch (IOException ex) {
            System.err.println(ex);
        } catch (SQLException ex) {
            ex.printStackTrace();

            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
