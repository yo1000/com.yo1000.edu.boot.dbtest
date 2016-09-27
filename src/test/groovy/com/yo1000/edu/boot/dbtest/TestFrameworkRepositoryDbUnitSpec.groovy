package com.yo1000.edu.boot.dbtest

import com.yo1000.dbspock.dbunit.DbspockLoaders
import com.yo1000.edu.boot.dbtest.Application.TestFrameworkRepository
import org.dbunit.DataSourceDatabaseTester
import org.dbunit.operation.DatabaseOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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

        databaseTester.dataSet = DbspockLoaders.loadDataSet {
            TEST_FRAMEWORK {
                col 'GROUP_ID'         | 'ARTIFACT_ID' | 'DESC'
                row 'com.ninja-squad'  | 'DbSetup'     | '小さいデータ向き。コードとデータを一緒に管理したい場合にオススメ。'
                row 'org.dbunit'       | 'dbunit'      | '大きいデータ向き。大量データの集計などをテストしたい場合にオススメ。'
                row 'Null Example'     | 'Null Example'| null
            }
        }

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