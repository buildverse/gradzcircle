module.exports = {
    preset: 'jest-preset-angular',
    setupTestFrameworkScriptFile: '<rootDir>/src/test/javascript/jest.ts',
    coverageDirectory: '<rootDir>/build/test-results/',
    globals: {
        'ts-jest': {
            tsConfigFile: 'tsconfig.json'
        },
        __TRANSFORM_HTML__: true
    },
    moduleNameMapper: {
        'app/(.*)': '<rootDir>/src/main/webapp/app/$1',
        '\\.(jpg|ico|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)': '<rootDir>/src/test/mocks/fileMock.js'
    },
    reporters: [
        'default',
        [ 'jest-junit', { output: './build/test-results/jest/TESTS-results.xml' } ]
    ],
    testResultsProcessor: 'jest-sonar-reporter',
    transformIgnorePatterns: ['node_modules/(?!@angular/common/locales)'],
    testMatch: ['<rootDir>/src/test/javascript/spec/**/+(*.)+(spec.ts)'],
    rootDir: '../../../',
    testURL: 'http://localhost/'
};
