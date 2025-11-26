import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../../services/api';
import { useDispatch } from 'react-redux';
import { setPageTitle } from '../../redux/uiSlice';
import { Spinner, Alert } from 'react-bootstrap';

interface CertificadoDados {
    id: number;
    nomeAluno: string;
    nomeCurso: string;
    nomeInstituicao: string;
    codValidacao: string;
    dataEmissao: string;
}

function VisualizarCertificadoPage() {
    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(setPageTitle("Visualizar Certificado"));
    }, [dispatch]);
    const { codigo } = useParams();
    const [dados, setDados] = useState<CertificadoDados | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const carregarCertificado = async () => {
            try {
                const response = await api.get(`/api/certificados/validar/${codigo}`);
                setDados(response.data);
            } catch (err) {
                setError('Certificado não encontrado ou inválido.');
            } finally {
                setLoading(false);
            }
        };
        carregarCertificado();
    }, [codigo]);

    if (loading) return <div style={{textAlign: 'center', marginTop: '50px'}}><Spinner animation="border" /> Carregando certificado...</div>;
    if (error) return <div style={{textAlign: 'center', marginTop: '50px'}}><Alert variant="danger">{error}</Alert></div>;
    if (!dados) return null;

    const styles = {
        body: {
            color: 'black',
            display: 'flex', 
            justifyContent: 'center',
            alignItems: 'center',
            fontFamily: 'Georgia, serif',
            fontSize: '24px',
            textAlign: 'center' as const,
            minHeight: '100vh',
            margin: 0,
            padding: 0,
            backgroundColor: '#f0f0f0'
        },
        container: {
            border: '20px solid tan',
            width: '750px',
            height: '563px',
            display: 'flex',
            flexDirection: 'column' as const,
            justifyContent: 'center',
            alignItems: 'center',
            backgroundColor: 'white',
            boxShadow: '0 0 10px rgba(0,0,0,0.1)'
        },
        logo: {
            color: 'tan',
            marginBottom: '10px'
        },
        marquee: {
            color: 'tan',
            fontSize: '48px',
            margin: '20px',
            textTransform: 'uppercase' as const
        },
        assignment: {
            margin: '20px',
        },
        person: {
            borderBottom: '2px solid black',
            fontSize: '32px',
            fontStyle: 'italic',
            margin: '20px auto',
            width: '400px',
            display: 'block'
        },
        reason: {
            margin: '20px',
        },
        footer: {
            fontSize: '14px',
            marginTop: '40px',
            color: '#555'
        }
    };

    return (
        <div style={styles.body}>
            <div style={styles.container}>
                
                <div style={styles.logo}>
                    {dados.nomeInstituicao}
                </div>

                <div style={styles.marquee}>
                    Certificado de Conclusão
                </div>

                <div style={styles.assignment}>
                    Certificamos que
                </div>

                <div style={styles.person}>
                    {dados.nomeAluno}
                </div>

                <div style={styles.reason}>
                    Concluiu com êxito o curso de<br/>
                    <strong>{dados.nomeCurso}</strong>
                </div>
                
                <div style={styles.footer}>
                    <p>Código de Validação: {dados.codValidacao}</p>
                </div>

            </div>
        </div>
    );
}

export default VisualizarCertificadoPage;