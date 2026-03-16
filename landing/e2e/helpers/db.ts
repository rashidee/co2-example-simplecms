import { exec } from 'child_process';
import { promisify } from 'util';

const execAsync = promisify(exec);

const DB_CONFIG = {
    host: 'localhost',
    port: '5432',
    user: 'cms_user',
    password: 'cms_password',
    database: 'cms_db',
};

export async function executeSql(sql: string): Promise<string> {
    const env = { ...process.env, PGPASSWORD: DB_CONFIG.password };
    const cmd = `psql -h ${DB_CONFIG.host} -p ${DB_CONFIG.port} -U ${DB_CONFIG.user} -d ${DB_CONFIG.database} -c "${sql.replace(/"/g, '\\"')}"`;
    const { stdout } = await execAsync(cmd, { env });
    return stdout;
}

export async function executeSqlFile(filePath: string): Promise<string> {
    const env = { ...process.env, PGPASSWORD: DB_CONFIG.password };
    const cmd = `psql -h ${DB_CONFIG.host} -p ${DB_CONFIG.port} -U ${DB_CONFIG.user} -d ${DB_CONFIG.database} -f "${filePath}"`;
    const { stdout } = await execAsync(cmd, { env });
    return stdout;
}
