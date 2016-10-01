package com.yo1000.edu.boot.dbtest

import com.ninja_squad.dbsetup.DbSetup
import com.ninja_squad.dbsetup.Operations
import com.ninja_squad.dbsetup.destination.DataSourceDestination
import com.yo1000.dbspock.dbsetup.DbspockOperations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.DataSource

@SpringBootTest
@Unroll
class TestFrameworkRepositoryDbSetupSpec extends Specification {
    @Autowired
    Application.TestFrameworkRepository frameworkRepository

    @Autowired
    DataSource dataSource

    def setup() {
        def destination = new DataSourceDestination(dataSource)

        def insertItems = DbspockOperations.insertInto {
            TEST_FRAMEWORK {
                GROUP_ID          | ARTIFACT_ID    | DESC
                'com.ninja-squad' | 'DbSetup'      | '小さいデータ向き。コードとデータを一緒に管理したい場合にオススメ。'
                'org.dbunit'      | 'dbunit'       | '大きいデータ向き。大量データの集計などをテストしたい場合にオススメ。'
                'Null Example'    | 'Null Example' | null
            }
        }

        new DbSetup(destination,
                Operations.sequenceOf(
                        Operations.truncate('TEST_FRAMEWORK'),
                        insertItems
                )
        ).launch()
    }

    def "dbsetup demo"() {
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