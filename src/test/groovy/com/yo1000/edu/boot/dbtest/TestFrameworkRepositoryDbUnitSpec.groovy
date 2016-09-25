package com.yo1000.edu.boot.dbtest

import com.yo1000.edu.boot.dbtest.Application.TestFrameworkRepository
import org.dbunit.DataSourceDatabaseTester
import org.dbunit.operation.DatabaseOperation
import org.dbunit.util.fileloader.CsvDataFileLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.ResourceUtils
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.DataSource

@SpringBootTest
@Unroll
class TestFrameworkRepositoryDbUnitSpec extends Specification {
    @Autowired
    TestFrameworkRepository frameworkRepository

    @Autowired
    DataSource dataSource

    def setup() {
        def databaseTester = new DataSourceDatabaseTester(dataSource)
        databaseTester.setUpOperation = DatabaseOperation.CLEAN_INSERT

        def loader = new CsvDataFileLoader()
        databaseTester.dataSet = loader.loadDataSet(ResourceUtils.getURL("classpath:dbunit/"))   // Requires tail slash

        databaseTester.onSetup()
    }

    def "dbunit demo"() {
        expect:

        def items = frameworkRepository.findAll()

        items.each {
            println 'groupId: ' + it.get('groupId')
            println 'artifactId: ' + it.get('artifactId')
            println 'desc: ' + it.get('desc')
            println 'desc is null?: ' + (it.get('desc') == null)
        }

        assert items.size() > 0
    }
}