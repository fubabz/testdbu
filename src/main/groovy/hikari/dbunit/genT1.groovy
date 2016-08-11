package hikari.dbunit

import groovy.xml.MarkupBuilder

class GenDataXmlX {
}

def names = [
    '松本',
    '中村',
    '鈴木',
    '佐藤',
    '斎藤',
    '上田',
    '島村'
]
def writer = new StringWriter()
def xml = new MarkupBuilder(writer)

xml.mkp.xmlDeclaration(version: 1.0, encoding: 'UTF-8')

xml.dataset() {
    //    mkp.comment('データセット定義')
    (1..5).each {i ->
        T1(ID:i, NAME:names[i%names.size()], AGE:20+i) {
        }
    }
}

println writer.toString()
